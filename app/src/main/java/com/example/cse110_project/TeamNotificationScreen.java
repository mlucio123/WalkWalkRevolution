package com.example.cse110_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.cse110_project.Firebase.InvitationCallback;
import com.example.cse110_project.Firebase.TeamCollection;
import com.example.cse110_project.notifications.InviteNotification;
import com.example.cse110_project.notifications.Notification;
import com.example.cse110_project.notifications.WalkNotification;
import com.example.cse110_project.notifications.WalkNotificationBuilder;
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
                                InviteNotification newInviteNotif =
                                        new InviteNotification(deviceId, notif.get("deviceID").toString(),
                                                teamID, deviceId.equals(notif.get("deviceID").toString()),
                                                notif.get("action").toString());
                                if (!newInviteNotif.getIsCreator()) {
                                    addNotification(newInviteNotif, findViewById(R.id.inviteResultContainer));
                                }
                            }
                        });

                        team.collection("responsesToWalk")
                                .get()
                                .addOnSuccessListener((teamNotifSnapShot) -> {
                                    for(DocumentSnapshot notif: teamNotifSnapShot.getDocuments()){
                                        WalkNotificationBuilder builder = new WalkNotificationBuilder();
                                        builder.isCreator(deviceId.equals(notif.get("deviceID").toString()))
                                                .fromName(notif.get("deviceID").toString())
                                                .result(notif.get("response").toString());
                                        WalkNotification newWalkNotif = builder.getNotification();
                                        if (!newWalkNotif.getIsCreator()) {
                                            addNotification(newWalkNotif, findViewById(R.id.walkNotifContainer));
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




    private void addNotification(Notification notification, LinearLayout container){
        int textColor = Color.parseColor("#FFFFFFFF");
        int textSize = 20;
        TextView add = new TextView(this);
        if(notification.getType() == Notification.NotifType.WalkNotification) {
            add.setText(notification.getFromName() + "'s response to your proposed walk is: " + notification.getResult());
        } else {
            add.setText(notification.getFromName() + " has " + notification.getResult() + " your team invitation");
        }
        add.setTextColor(textColor);
        add.setTextSize(textSize);
        //ensure only notifs from other teammates are displayed
        container.addView(add);
    }










}
