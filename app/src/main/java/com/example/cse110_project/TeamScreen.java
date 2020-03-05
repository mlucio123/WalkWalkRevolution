package com.example.cse110_project;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.MenuItem;

import com.example.cse110_project.Firebase.MyCallback;
import com.example.cse110_project.Firebase.RouteCollection;
import com.example.cse110_project.Firebase.TeamCollection;
import com.example.cse110_project.Firebase.UserCollection;
import com.example.cse110_project.utils.AccessSharedPrefs;
import com.example.cse110_project.utils.Route;
import com.example.cse110_project.utils.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TeamScreen extends AppCompatActivity {

    private String fitnessServiceKey = "GOOGLE_FIT";
    private BottomNavigationView bottomNavigationView;

    private Button createTeamBtn;
    private Button inviteBtn;
    private EditText inviteeEmail;
    private TextView inviteeLabel;
    private String TAG = "Team Screen: ";

    public static boolean testing = false;

    public boolean hasTeam = false;

    public void setHasTeam(boolean newValue) { this.hasTeam = newValue; }

    public boolean getHasTean() { return this.hasTeam; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_screen);

        //TODO: set ui based on whether user is on a team

        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        Log.d(TAG, "created");
        RouteCollection.initFirebase(this);
        UserCollection.initFirebase(this);
        TeamCollection.initFirebase(this);

        createTeamBtn = (Button) findViewById(R.id.createTeam);
        inviteBtn = (Button) findViewById(R.id.inviteMemberBtn);
        inviteeEmail = (EditText) findViewById(R.id.inviteEmail);
        inviteeLabel = (TextView) findViewById(R.id.inviteEmailLabel);


        createTeamBtn.setVisibility(View.GONE);
        inviteBtn.setVisibility(View.GONE);
        inviteeEmail.setVisibility(View.GONE);
        inviteeLabel.setVisibility(View.GONE);

        if(AccessSharedPrefs.getOnTeam(this)) {
            renderOnATeamUI();
        }


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
                            inviteBtn.setVisibility(View.VISIBLE);
                            inviteeEmail.setVisibility(View.VISIBLE);
                            inviteeLabel.setVisibility(View.VISIBLE);
                            setHasTeam(true);
                        } else {
                            createTeamBtn.setVisibility(View.VISIBLE);
                        }
                    } else {
                        createTeamBtn.setVisibility(View.VISIBLE);
                    }
                }
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
//                AccessSharedPrefs.saveTeamID(TeamScreen.this, newTeamId);

                AccessSharedPrefs.saveOnTeam(TeamScreen.this);
                Toast.makeText(TeamScreen.this, "Team Created!", Toast.LENGTH_SHORT).show();

                //render team screen ui
                createTeamBtn.setVisibility(View.GONE);
                inviteBtn.setVisibility(View.VISIBLE);
                inviteeEmail.setVisibility(View.VISIBLE);
                inviteeLabel.setVisibility(View.VISIBLE);
                //etc
            }
        });

        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inviteeEmail.getText().toString();

                TeamCollection tc = new TeamCollection();

                // tc.sendInvitationEmail(email, teamID, currUserID);
                tc.sendInviteToEmail(email, deviceID);
                Toast.makeText(TeamScreen.this, "Invitation Sent!", Toast.LENGTH_SHORT).show();
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

    public void renderOnATeamUI() {
        createTeamBtn.setVisibility(View.GONE);
        inviteBtn.setVisibility(View.VISIBLE);
        inviteeEmail.setVisibility(View.VISIBLE);
        inviteeLabel.setVisibility(View.VISIBLE);
    }


    private void selectFragment(MenuItem item){

        Intent newIntent = new Intent(this, this.getClass());
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
