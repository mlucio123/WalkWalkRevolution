package com.example.cse110_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse110_project.Firebase.RouteCollection;
import com.example.cse110_project.Firebase.TeamCollection;
import com.example.cse110_project.Firebase.UserCollection;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.example.cse110_project.Firebase.PendingTeammatesListListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddTeamateScreen extends AppCompatActivity {
    private String TAG = "ADD TEAMMATE SCREEN: ";
    private String fitnessServiceKey = "GOOGLE_FIT";
    private BottomNavigationView bottomNavigationView;
    private Button addTeammateBtn;
    private EditText inviteeName;
    private EditText inviteeEmail;


    private Button submitBtn;
    private Button createTeamBtn;


    private LinearLayout invitationLayout;
    private LinearLayout createTeamLayout;


    public static boolean testing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_teamate_screen);

        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        RouteCollection.initFirebase(this);
        UserCollection.initFirebase(this);
        TeamCollection.initFirebase(this);

        submitBtn = findViewById(R.id.submitBtn);
        createTeamBtn = findViewById(R.id.createTeamBtn);
        inviteeEmail = findViewById(R.id.inviteeEmail);


        invitationLayout = findViewById(R.id.invitationLayout);
        createTeamLayout = findViewById(R.id.createTeamLayout);

        invitationLayout.setVisibility(View.GONE);
        createTeamLayout.setVisibility(View.GONE);


        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = rootRef.collection("users").document(deviceID);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.get("teamID") != null) {
                            Log.d(TAG, "THIS USER:" + deviceID + " HAS A TEAM!");
                            invitationLayout.setVisibility(View.VISIBLE);
                        } else {
                            createTeamLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        createTeamLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });


        createTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make new team in database
                Log.d(TAG, "Making new team");
                TeamCollection tc = new TeamCollection();
                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                tc.makeTeam(deviceID);
                createTeamLayout.setVisibility(View.GONE);
                invitationLayout.setVisibility(View.VISIBLE);
                Toast.makeText(AddTeamateScreen.this, "Team Created!", Toast.LENGTH_SHORT).show();

            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 String email = inviteeEmail.getText().toString();

                 TeamCollection tc = new TeamCollection();

                 // tc.sendInvitationEmail(email, teamID, currUserID);
                 Log.d(TAG, "SENDING INVITATION TO " + email + " from " + deviceID);
                 tc.sendInviteToEmail(email, deviceID);
                 Toast.makeText(AddTeamateScreen.this, "Invitation Sent!", Toast.LENGTH_SHORT).show();
                 finish();
             }
         });

        inviteeName = findViewById(R.id.inviteeName);
        inviteeEmail = findViewById(R.id.inviteeEmail);

        Intent intent = new Intent(this, TeamScreen.class);

        // Configure addTeammateBtn, register listener
        addTeammateBtn = findViewById(R.id.submitBtn);
        addTeammateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inviteeNameText = inviteeName.getText().toString();
                String inviteeEmailText = inviteeEmail.getText().toString();
                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                TeamCollection tc = new TeamCollection();
                tc.addToTeamPendingList(deviceID, inviteeNameText, inviteeEmailText, new PendingTeammatesListListener() {
                    @Override
                    public void onSuccess() {
                        startActivity(intent);
                    }
                });
                // Check if user has a team
                // create team if not and add user
                // else get team id
                // Add new user to pending list
                // send invitation

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

    }



        private void selectFragment(MenuItem item) {

            Intent newIntent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    newIntent = new Intent(AddTeamateScreen.this, HomeScreen.class);
                    newIntent.putExtra(HomeScreen.FITNESS_SERVICE_KEY, fitnessServiceKey);
                    startActivity(newIntent);
                    break;
                case R.id.navigation_walk:
                    newIntent = new Intent(AddTeamateScreen.this, WalkScreen.class);
                    startActivity(newIntent);
                    break;
                case R.id.navigation_routes:
                    newIntent = new Intent(AddTeamateScreen.this, RouteScreen.class);
                    startActivity(newIntent);
                    break;
                default:
                    break;
            }
        }



}

