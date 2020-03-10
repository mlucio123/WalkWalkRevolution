package com.example.cse110_project;

import android.opengl.Visibility;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.content.Intent;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;



import com.example.cse110_project.Firebase.ProposeWalkCollection;
import com.example.cse110_project.Firebase.RouteCollection;
import com.example.cse110_project.Firebase.TeamCollection;
import com.example.cse110_project.Firebase.UserCollection;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.example.cse110_project.Firebase.TeammatesListListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class TeamScreen extends AppCompatActivity {

    private String fitnessServiceKey = "GOOGLE_FIT";
    private BottomNavigationView bottomNavigationView;
    private Button addTeamateBtn;

//    private MyRecyclerViewAdapter adapter;
    private LinearLayout proposedWalkLayout;


    private Button inviteBtn;
    private ImageButton notifsButton;
    private EditText inviteeEmail;
    private TextView inviteeLabel;
    private String TAG = "Team Screen: ";

    private TextView propWalkLabel;
    private TextView startingPointLabel;
    private TextView timeLabel;
    private TextView proposerLabel;

    private Button acceptButton;
    private Button badTimeButton;
    private Button badRouteButton;

    private Button scheduleButton;
    private Button withdrawButton;

    private TextView isScheduleText;
    private TextView acceptPpl;
    private TextView badTimePpl;
    private TextView badRoutePpl;

    private final ArrayList<String> acceptsPplList = new ArrayList<>();
    private final ArrayList<String> declineTimeList = new ArrayList<>();
    private final ArrayList<String> declineRouteList = new ArrayList<>();

    public static boolean testing = false;

    public boolean hasTeam = false;

    private MultiViewTypeAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_screen);

        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Initialize Firebase Collections
        Log.d(TAG, "created");
        RouteCollection.initFirebase(this);
        UserCollection.initFirebase(this);
        TeamCollection.initFirebase(this);

        inviteBtn = (Button) findViewById(R.id.addBtn);

        //enable team notif screen
        notifsButton = findViewById(R.id.walkNotifButton);
        notifsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchNotifScreen();
            }
        });

        propWalkLabel = findViewById(R.id.walkTitleText);
        startingPointLabel = findViewById(R.id.startingPoint);
        timeLabel = findViewById(R.id.walkStartTime);
        proposerLabel = findViewById(R.id.createdBy);

        acceptButton = findViewById(R.id.acceptWalkButton);
        badTimeButton = findViewById(R.id.badTimeDeclineBtn);
        badRouteButton = findViewById(R.id.badRouteDeclineBtn);

        scheduleButton = findViewById(R.id.scheduleWalkBtn);
        withdrawButton = findViewById(R.id.withdrawWalkBtn);

        acceptPpl = findViewById(R.id.acceptsPeople);
        badTimePpl = findViewById(R.id.badTimePeople);
        badRoutePpl = findViewById(R.id.badRoutePeople);

        acceptButton.setVisibility(View.GONE);
        badTimeButton.setVisibility(View.GONE);
        badRouteButton.setVisibility(View.GONE);
        scheduleButton.setVisibility(View.GONE);
        withdrawButton.setVisibility(View.GONE);

        isScheduleText = findViewById(R.id.isScheduledText);

        Log.d(TAG, "hiding proposed walk layout");
        proposedWalkLayout = findViewById(R.id.proposedWalkLayout);
        proposedWalkLayout.setVisibility(View.GONE);


        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        rootRef.collection("users")
                .document(deviceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                       DocumentSnapshot rootDoc = task.getResult();

                       String returnedTeamID = "";

                       if(rootDoc.getData().get("teamID") != null ) {
                           returnedTeamID = rootDoc.getData().get("teamID").toString();
                           subscribeToNotificationsTopic(returnedTeamID);

                           DocumentReference docIdRef = rootRef
                                   .collection("teams")
                                   .document(returnedTeamID)
                                   .collection("proposeWalk")
                                   .document(returnedTeamID);
                           docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                   Log.d(TAG, "Checking if team has proposed walk");
                                   if (task.isSuccessful()) {
                                       DocumentSnapshot document = task.getResult();
                                       //set fields

                                       if (document.getData() == null) {
                                           Log.d(TAG, "team has no proposed walk");

                                           proposedWalkLayout.setVisibility(View.GONE);
                                       } else {

                                           Log.d(TAG, "team has proposed walk, showing info");

                                           proposedWalkLayout.setVisibility(View.VISIBLE);

                                           Log.d(TAG, "DATA: " + document.getData());
                                           String proposedByID = document.getData().get("proposedBy").toString();

                                           rootRef.collection("users")
                                                   .document(proposedByID)
                                                   .get()
                                                   .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                      @Override
                                                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                          try {
                                                              proposerLabel.setText(task.getResult().getData().get("initial").toString());
                                                          } catch(Exception e) {
                                                              Log.d(TAG, "ERROR " + e.toString());
                                                          }
                                                      }
                                                  });



                                           if (document.getData().get("routeSTart") != null) {
                                               startingPointLabel.setText(document.getData().get("routeSTart").toString());
                                           }

                                           String theTime = "";

                                           theTime = document.getData().get("hour").toString() + ":" + document.getData().get("minute").toString() + " " + document.getData().get("month") + "/" + document.getData().get("day") + "/" + document.getData().get("year");


                                           String isSchStr = document.getData().get("isScheduled").toString();

                                           boolean val = Boolean.parseBoolean(isSchStr);

                                           if(val) {
                                               isScheduleText.setText("This walk is scheduled!");
                                           } else {
                                               isScheduleText.setText("Not yet!");
                                           }

                                           timeLabel.setText(theTime);

//                                           proposerLabel.setText(proposedBy);

                                           if (deviceID.equals(proposedByID)) {
                                               scheduleButton.setVisibility(View.VISIBLE);
                                               withdrawButton.setVisibility(View.VISIBLE);
                                           } else {
                                               acceptButton.setVisibility(View.VISIBLE);
                                               badTimeButton.setVisibility(View.VISIBLE);
                                               badRouteButton.setVisibility(View.VISIBLE);
                                           }
                                       }


                                   }
                               }
                           });

                           rootRef.collection("teams")
                                   .document(returnedTeamID)
                                   .collection("responsesToWalk")
                                   .whereEqualTo("response", "join walk")
                                   .get()
                                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                       @Override
                                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                           if (task.isSuccessful()) {

                                               Log.d(TAG, "RECEIVED RESULT COUNT : " + task.getResult().size());

                                               for (QueryDocumentSnapshot document : task.getResult()) {
                                                   Log.d(TAG, "Retrieving replies for  => " + document.getData());

                                                   String userID = document.getData().get("deviceID").toString();

                                                   FirebaseFirestore thirdCheck = FirebaseFirestore.getInstance();


                                                   thirdCheck.collection("users")
                                                           .document(userID)
                                                           .get()
                                                           .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                               @Override
                                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                   if (task.isSuccessful()) {
                                                                       DocumentSnapshot document = task.getResult();
                                                                       if (document.exists()) {
                                                                           String initial = document.getData().get("initial").toString();
                                                                           Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                                                           acceptsPplList.add(initial);
                                                                           acceptPpl.setText(acceptsPplList.toString());

                                                                       }
                                                                   }
                                                               }
                                                           });

                                               }

                                           } else {
                                               Log.d(TAG, "Error getting documents: ", task.getException());
                                           }
                                       }
                                   })
                                   .addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Log.w(TAG, "Error proposing a walk document", e);
                                       }
                                   });


                           rootRef.collection("teams")
                                   .document(returnedTeamID)
                                   .collection("responsesToWalk")
                                   .whereEqualTo("response", "bad time")
                                   .get()
                                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                       @Override
                                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                           if (task.isSuccessful()) {

                                               Log.d(TAG, "RECEIVED RESULT COUNT : " + task.getResult().size());

                                               for (QueryDocumentSnapshot document : task.getResult()) {
                                                   Log.d(TAG, "Retrieving replies for  => " + document.getData());

                                                   String userID = document.getData().get("deviceID").toString();

                                                   FirebaseFirestore thirdCheck = FirebaseFirestore.getInstance();


                                                   thirdCheck.collection("users")
                                                           .document(userID)
                                                           .get()
                                                           .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                               @Override
                                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                   if (task.isSuccessful()) {
                                                                       DocumentSnapshot document = task.getResult();
                                                                       if (document.exists()) {
                                                                           String initial = document.getData().get("initial").toString();
                                                                           Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                                                           declineTimeList.add(initial);
                                                                           badTimeButton.setText(declineTimeList.toString());

                                                                       }
                                                                   }
                                                               }
                                                           });

                                               }

                                           } else {
                                               Log.d(TAG, "Error getting documents: ", task.getException());
                                           }
                                       }
                                   })
                                   .addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Log.w(TAG, "Error proposing a walk document", e);
                                       }
                                   });


                           rootRef.collection("teams")
                                   .document(returnedTeamID)
                                   .collection("responsesToWalk")
                                   .whereEqualTo("response", "bad route")
                                   .get()
                                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                       @Override
                                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                           if (task.isSuccessful()) {

                                               Log.d(TAG, "RECEIVED RESULT COUNT : " + task.getResult().size());

                                               for (QueryDocumentSnapshot document : task.getResult()) {
                                                   Log.d(TAG, "Retrieving replies for  => " + document.getData());

                                                   String userID = document.getData().get("deviceID").toString();

                                                   FirebaseFirestore thirdCheck = FirebaseFirestore.getInstance();


                                                   thirdCheck.collection("users")
                                                           .document(userID)
                                                           .get()
                                                           .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                               @Override
                                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                   if (task.isSuccessful()) {
                                                                       DocumentSnapshot document = task.getResult();
                                                                       if (document.exists()) {
                                                                           String initial = document.getData().get("initial").toString();
                                                                           Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                                                           declineRouteList.add(initial);
                                                                           badRoutePpl.setText(declineRouteList.toString());

                                                                       }
                                                                   }
                                                               }
                                                           });

                                               }

                                           } else {
                                               Log.d(TAG, "Error getting documents: ", task.getException());
                                           }
                                       }
                                   })
                                   .addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Log.w(TAG, "Error proposing a walk document", e);
                                       }
                                   });
                       }
                   }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error proposing a walk document", e);
                    }
                });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamCollection tc = new TeamCollection();
                tc.setUserResponseToWalk(deviceID, "join walk");
                Toast.makeText(TeamScreen.this, "Joining Walk!", Toast.LENGTH_SHORT).show();

            }
        });

        badTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamCollection tc = new TeamCollection();
                tc.setUserResponseToWalk(deviceID, "bad time");
                Toast.makeText(TeamScreen.this, "Bad Time!", Toast.LENGTH_SHORT).show();

            }
        });

        badRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamCollection tc = new TeamCollection();
                tc.setUserResponseToWalk(deviceID, "bad route");
                Toast.makeText(TeamScreen.this, "Bad Route!", Toast.LENGTH_SHORT).show();
            }
        });

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProposeWalkCollection pwc = new ProposeWalkCollection();
                pwc.setScheduled(deviceID);
                isScheduleText.setText("This walk is scheduled!");
                Toast.makeText(TeamScreen.this, "Scheduled Proposed Walk!", Toast.LENGTH_SHORT).show();
            }
        });


        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProposeWalkCollection pwc = new ProposeWalkCollection();
                pwc.withdrawWalk(deviceID);
                proposedWalkLayout.setVisibility(View.GONE);
                Toast.makeText(TeamScreen.this, "Withdraw Proposed Walk!", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize teamBtn and bottom navigation bar
        addTeamateBtn = (Button) findViewById(R.id.addBtn);
        Intent intent = new Intent(this, AddTeamateScreen.class);
        addTeamateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return false;
            }
        });

        // Fetch list of teammates from database
        Log.i(TAG, deviceID);

        // Get team id

        // Get list of User IDs

        // Get list of Pending IDs

        // Create Teamate Model list

//        Button proposeWalk = (Button) findViewById(R.id.ppWalkBtn);
//
//        proposeWalk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                launchProposeWalkScreen();
//            }
//        });

        // Get team id
        UserCollection uc = new UserCollection();
        ArrayList<TeamateModel> list= new ArrayList();

        MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(list, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        TeammatesListListener tm = new TeammatesListListener() {
            @Override
            public void onSuccess(String name) {
                return;
            }
        };
        // Get list of User IDs
        uc.getTeammatesList(deviceID, new TeammatesListListener() {
            @Override
            public void onSuccess(String name) {
                // Create Teammate Model list
                list.add(new TeamateModel(TeamateModel.ACCEPT_TYPE,name));
                adapter.notifyDataSetChanged();
                Log.i(TAG, "TEAMMATE LIST: " + list.toString());
                Log.i(TAG, "TEAMMATE LIST SIZE: " + adapter.getItemCount());
            }
        });

        // Get list of pending User IDs
        uc.getPendingTeammatesList(deviceID, new TeammatesListListener() {
            @Override
            public void onSuccess(String name) {
                list.add(new TeamateModel(TeamateModel.PENDING_TYPE,name));
                adapter.notifyDataSetChanged();
            }
        });


        // Get list of Pending IDs
    }


    public void launchProposeWalkScreen() {
        Intent intent = new Intent(this, ProposeWalkScreen.class);
        startActivity(intent);
    }

    public void launchNotifScreen() {
        Intent intent = new Intent(this, TeamNotificationScreen.class);
        startActivity(intent);
    }

    /*
        ArrayList<TeamateModel> list= new ArrayList();
//        list.add(new TeamateModel(TeamateModel.ACCEPT_TYPE,"JINING"));
//        list.add(new TeamateModel(TeamateModel.PENDING_TYPE,"HOWARD"));
//        list.add(new TeamateModel(TeamateModel.PENDING_TYPE,"CONOR"));
//        list.add(new TeamateModel(TeamateModel.PENDING_TYPE,"MIA"));

        MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(list,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

    }
    */
//    public void renderOnATeamUI() {
//        createTeamBtn.setVisibility(View.GONE);
//        inviteBtn.setVisibility(View.VISIBLE);
//        inviteeEmail.setVisibility(View.VISIBLE);
//        inviteeLabel.setVisibility(View.VISIBLE);
//    }

    private void subscribeToNotificationsTopic(String teamID) {

        FirebaseMessaging.getInstance().subscribeToTopic(teamID)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(TeamScreen.this, msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }


    private void selectFragment(MenuItem item){

        Intent newIntent;
        switch(item.getItemId()) {
            case R.id.navigation_home:
                newIntent = new Intent(this, HomeScreen.class);
                newIntent.putExtra(HomeScreen.FITNESS_SERVICE_KEY, fitnessServiceKey);
                startActivity(newIntent);
                break;
            case R.id.navigation_walk:
                newIntent = new Intent(this, WalkScreen.class);
                startActivity(newIntent);
                break;
            case R.id.navigation_routes:
                newIntent = new Intent(this, RouteScreen.class);
                startActivity(newIntent);
                break;
            case R.id.navigation_team:
                break;
            default:
                break;
        }
    }

}



