package com.example.cse110_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.cse110_project.Firebase.InvitationCallback;
import com.example.cse110_project.Firebase.TeamCollection;
import com.example.cse110_project.notifications.InviteNotification;
import com.example.cse110_project.utils.AccessSharedPrefs;
import com.example.cse110_project.utils.Team;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.Distribution;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TeamNotificationScreen extends AppCompatActivity {
    String TAG = TeamNotificationScreen.class.getSimpleName();
    ImageButton cancel;
    FirebaseFirestore db;
    String deviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_notification_screen);

        cancel = (ImageButton) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamNotificationScreen.this, HomeScreen.class );
                startActivity(intent);
            }
        });
        int textColor = Color.parseColor("#FFFFFFFF");
        deviceId = AccessSharedPrefs.getUserID(TeamNotificationScreen.this);
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("deviceID", deviceId)
                .addSnapshotListener((userSnapShot, error) -> {
            for(DocumentSnapshot userDoc : userSnapShot.getDocuments()){
                String teamID = userDoc.get("teamID").toString();
                DocumentReference team = db.collection("teams")
                        .document(teamID);
                        team.collection("responses")
                        .get()
                        .addOnSuccessListener((teamNotifSnapShot) -> {
                            for(DocumentSnapshot notif: teamNotifSnapShot.getDocuments()){
                                String response = notif.get("action").toString();
                                String fromDevice = notif.get("deviceID").toString();
                                TextView add = new TextView(this);
                                add.setText(fromDevice + " has " + response +" your team invitation");
                                add.setTextColor(textColor);
                                add.setTextSize(20);
                                LinearLayout contain = findViewById(R.id.inviteResultContainer);
                                if (!deviceId.equals(fromDevice)) {
                                    contain.addView(add);
                                }
                            }
                        });

                        team.collection("responsesToWalk")
                                .get()
                                .addOnSuccessListener((teamNotifSnapShot) -> {
                                    for(DocumentSnapshot notif: teamNotifSnapShot.getDocuments()){
                                        String response = notif.get("response").toString();
                                        String fromDevice = notif.get("deviceID").toString();
                                        TextView add = new TextView(this);
                                        add.setText(fromDevice + "'s response to your proposed walk is " + response);
                                        add.setTextColor(textColor);
                                        add.setTextSize(20);
                                        LinearLayout contain = findViewById(R.id.inviteResultContainer);
                                        //ensure only notifs from other teammates are displayed
                                        if (!deviceId.equals(fromDevice)) {
                                            contain.addView(add);
                                        }
                                    }
                                });
            }
        });



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


    }

}
