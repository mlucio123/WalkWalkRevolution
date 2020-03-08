package com.example.cse110_project;

import android.os.Bundle;

import com.example.cse110_project.Firebase.InvitationCallback;
import com.example.cse110_project.Firebase.TeamCollection;
import com.example.cse110_project.notifications.InviteNotification;
import com.example.cse110_project.utils.Team;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.Distribution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TeamNotificationScreen extends AppCompatActivity {

    private String TAG = "TEAM NOTIF SCREEN : ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_notification_screen);


        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        InviteNotification in = new InviteNotification(deviceID);

        in.getUserRepsonses(deviceID, new InvitationCallback() {
            @Override
            public void getUsers(String userInitial, String action) {
                Log.d(TAG, userInitial + " has " + action + " to join the TEAM");

            }
        });


        TeamCollection tc = new TeamCollection();
        tc.getWalkingResponses(deviceID, new InvitationCallback() {
            @Override
            public void getUsers(String userInitial, String action) {
                Log.d(TAG, userInitial + " has responded " + action + " for the WALK");
            }
        });

        /*ImageButton backBtn = findViewById(R.id.backButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        //check for proposed walk
        //if proposed walk exists, show proposed walk layout

    }

}
