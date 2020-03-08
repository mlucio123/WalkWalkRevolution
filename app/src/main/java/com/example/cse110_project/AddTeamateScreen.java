package com.example.cse110_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse110_project.Firebase.RouteCollection;
import com.example.cse110_project.Firebase.TeamCollection;
import com.example.cse110_project.Firebase.UserCollection;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class AddTeamateScreen extends AppCompatActivity {
    private String TAG = "ADD TEAMMATE SCREEN: ";
    private String fitnessServiceKey = "GOOGLE_FIT";
    private BottomNavigationView bottomNavigationView;
    private Button addTeammateBtn;
    private EditText inviteeName;
    private EditText inviteeEmail;


    public static boolean testing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_teamate_screen);

        RouteCollection.initFirebase(this);
        UserCollection.initFirebase(this);
        TeamCollection.initFirebase(this);


        inviteeName = findViewById(R.id.inviteeName);
        inviteeEmail = findViewById(R.id.inviteeEmail);
        // Configure addTeammateBtn, register listener
        addTeammateBtn = findViewById(R.id.submitBtn);
        addTeammateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if user has a team
                    // create team if not and add user
                    // else get team id
                // Add new user to pending list
                // send invitation


                Log.i(TAG, inviteeName.getText().toString());
                Log.i(TAG, inviteeEmail.getText().toString());

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
