package com.example.cse110_project;

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
import android.widget.TextView;


import com.example.cse110_project.Firebase.RouteCollection;
import com.example.cse110_project.Firebase.TeamCollection;
import com.example.cse110_project.Firebase.UserCollection;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class TeamScreen extends AppCompatActivity {

    private String fitnessServiceKey = "GOOGLE_FIT";
    private BottomNavigationView bottomNavigationView;
    private Button addTeamateBtn;
    private MyRecyclerViewAdapter adapter;


    private Button inviteBtn;
    private ImageButton notifsButton;
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

        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        notifsButton = findViewById(R.id.walkNotifButton);
        notifsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchNotifScreen();
            }
        });

        // Initialize Firebase Collections
        Log.d(TAG, "created");
        RouteCollection.initFirebase(this);
        UserCollection.initFirebase(this);
        TeamCollection.initFirebase(this);

        inviteBtn = (Button) findViewById(R.id.addBtn);



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

