package com.example.cse110_project;

import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


import com.example.cse110_project.Firebase.RouteCollection;
import com.example.cse110_project.Firebase.TeamCollection;
import com.example.cse110_project.Firebase.UserCollection;

import com.example.cse110_project.utils.AccessSharedPrefs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class TeamScreen extends AppCompatActivity {

    private String fitnessServiceKey = "GOOGLE_FIT";
    private BottomNavigationView bottomNavigationView;
    private Button addTeamateBtn;
    private MyRecyclerViewAdapter adapter;
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

    public static boolean testing = false;

    public boolean hasTeam = false;

    public void setHasTeam(boolean newValue) { this.hasTeam = newValue; }

    public boolean getHasTean() { return this.hasTeam; }

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

        propWalkLabel = findViewById(R.id.walkTitleField);
        startingPointLabel = findViewById(R.id.startingPoint);
        timeLabel = findViewById(R.id.walkStartTime);
        proposerLabel = findViewById(R.id.createdBy);

        acceptButton = findViewById(R.id.acceptWalkButton);
        badTimeButton = findViewById(R.id.badTimeDeclineBtn);
        badRouteButton = findViewById(R.id.badRouteDeclineBtn);

        scheduleButton = findViewById(R.id.scheduleWalkBtn);
        withdrawButton = findViewById(R.id.withdrawWalkBtn);

        acceptButton.setVisibility(View.GONE);
        badTimeButton.setVisibility(View.GONE);
        badRouteButton.setVisibility(View.GONE);
        scheduleButton.setVisibility(View.GONE);
        withdrawButton.setVisibility(View.GONE);

        Log.d(TAG, "hiding proposed walk layout");
        proposedWalkLayout = findViewById(R.id.proposedWalkLayout);
        proposedWalkLayout.setVisibility(View.GONE);

        Log.d(TAG, "Getting team with id " + deviceID);
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
                                           String proposedBy = document.getData().get("proposedBy").toString();

                                           propWalkLabel.setText(document.getData().get("walkingName").toString());

                                           if (document.getData().get("routeSTart") != null) {
                                               startingPointLabel.setText(document.getData().get("routeSTart").toString());
                                           }

                                           String theTime = "";

                                           theTime = document.getData().get("hour").toString() + ":" + document.getData().get("minute").toString() + " " + document.getData().get("month") + "/" + document.getData().get("day") + "/" + document.getData().get("year");


                                           timeLabel.setText(theTime);

                                           proposerLabel.setText(proposedBy);


                                           if (deviceID.equals(proposedBy)) {
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

        Button proposeWalk = (Button) findViewById(R.id.ppWalkBtn);

        proposeWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchProposeWalkScreen();
            }
        });

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


//
//        createTeamBtn = (Button) findViewById(R.id.createTeam);
//        inviteBtn = (Button) findViewById(R.id.inviteMemberBtn);
//        inviteeEmail = (EditText) findViewById(R.id.inviteEmail);
//        inviteeLabel = (TextView) findViewById(R.id.inviteEmailLabel);
//
//
//        String teamID = AccessSharedPrefs.getTeamID(TeamScreen.this);
//
//        if (teamID.length() == 0){
//            inviteBtn.setVisibility(View.GONE);
//            inviteeEmail.setVisibility(View.GONE);
//            inviteeLabel.setVisibility(View.GONE);
//        } else {
//            createTeamBtn.setVisibility(View.GONE);
//        }
//
//        createTeamBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //make new team in database
//                Log.d(TAG, "Making new team");
//                TeamCollection tc = new TeamCollection();
//                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//                String newTeamId = tc.makeTeam(deviceID);
//                AccessSharedPrefs.saveTeamID(TeamScreen.this, newTeamId);
//
//                Toast.makeText(TeamScreen.this, "Team Created!", Toast.LENGTH_SHORT).show();
//
//                //render team screen ui
//                createTeamBtn.setVisibility(View.GONE);
//                inviteBtn.setVisibility(View.VISIBLE);
//                inviteeEmail.setVisibility(View.VISIBLE);
//                inviteeLabel.setVisibility(View.VISIBLE);
//                //etc
//            }
//        });
//
//        inviteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String email = inviteeEmail.getText().toString();
//                String currUserID = AccessSharedPrefs.getUserID(TeamScreen.this);
//                String teamID = AccessSharedPrefs.getTeamID(TeamScreen.this);
//                TeamCollection tc = new TeamCollection();
//
//                tc.sendInvitationEmail(email, teamID, currUserID);
//                Toast.makeText(TeamScreen.this, "Invitation Sent!", Toast.LENGTH_SHORT).show();
//            }
//        });

