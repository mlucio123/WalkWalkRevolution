package com.example.cse110_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.cse110_project.notifications.InviteNotification;
import com.example.cse110_project.notifications.Notification;
import com.example.cse110_project.notifications.WalkNotification;
import com.example.cse110_project.notifications.WalkNotificationBuilder;
import com.example.cse110_project.utils.AccessSharedPrefs;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.zip.Inflater;

public class NotificationScreen extends AppCompatActivity {
    String TAG = NotificationScreen.class.getSimpleName();
    ImageButton cancel;
    CollectionReference chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_screen);
        cancel = (ImageButton) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationScreen.this, HomeScreen.class );
                startActivity(intent);
            }
        });

        final String currUserID = AccessSharedPrefs.getUserID(NotificationScreen.this);
        Log.d("Notification: ", "This is user's id " + currUserID);
        chat = FirebaseFirestore.getInstance().collection("users");

        // Create a query against the collection.
        Query query = chat.whereEqualTo("deviceID", currUserID);
        int textColor = Color.parseColor("#FFFFFFFF");
        int bodySize = 20;
        int headerSize = 30;

        //retrieve  query results asynchronously using query.get()
        query.addSnapshotListener((newChatSnapShot, error) -> {
            for (DocumentSnapshot document : newChatSnapShot.getDocuments()) {
                CollectionReference invite = document.getReference().collection("invitations");
                invite.addSnapshotListener((newSnapShot, err) -> {
                    for(DocumentSnapshot doc : newSnapShot.getDocuments()) {
                        StringBuilder header = new StringBuilder();
                        StringBuilder body = new StringBuilder();
                        header.append("You have a team invite from ");
                        header.append(doc.get("fromUserID"));
                        header.append("!");
                        body.append("Please accept or decline your invite to team: ");
                        body.append(doc.get("teamId"));

                        Drawable draw = getDrawable(R.drawable.rounded_edges);
                        LinearLayout notifContainer = findViewById(R.id.notif_container);
                        LinearLayout newNotifHolder = new LinearLayout(this);
                        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        InviteNotification i = new InviteNotification(currUserID, doc.get("fromUserID").toString(), doc.get("teamId").toString());
                        newNotifHolder.setOrientation(LinearLayout.VERTICAL);
                        newNotifHolder.setBackground(draw);
                        newNotifHolder.setLayoutParams(containerParams);

                        Drawable buttonBack = getDrawable(R.drawable.btn_rounded);
                        Drawable buttonBackRed = getDrawable(R.drawable.btn_rounded_red);
                        final Button declineBtn = new Button(NotificationScreen.this);
                        declineBtn.setBackground(buttonBackRed);
                        declineBtn.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        declineBtn.setText("Decline");
                        declineBtn.setTag(i);
                        declineBtn.setTextColor(textColor);
                        declineBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                InviteNotification inviteNotification = (InviteNotification) view.getTag();
                                inviteNotification.deleteNotification( currUserID, document.getId(),doc.get("teamId").toString(), doc.getId());
                                newNotifHolder.setVisibility(View.GONE);
                            }
                        });

                        final Button acceptBtn = new Button(NotificationScreen.this);
                        acceptBtn.setBackground(buttonBack);
                        acceptBtn.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));

                        acceptBtn.setText("Accept");
                        acceptBtn.setTextColor(textColor);
                        acceptBtn.setTag(i);
                        acceptBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "STARTING TO ACCEPT INVITE");
                                InviteNotification inviteNotification = (InviteNotification) v.getTag();
                                Log.d(TAG, "SECOND STEP PASSED");
                                inviteNotification.addUserToTeamList(currUserID, doc.get("teamId").toString());
                                Toast.makeText(NotificationScreen.this, "Accepted invitation clicked", Toast.LENGTH_SHORT).show();
                                inviteNotification.deleteNotification( currUserID, document.getId(),doc.get("teamId").toString(), doc.getId());
                                newNotifHolder.setVisibility(View.GONE);
                            }
                        });

                        TextView head = new TextView(this);
                        head.setTextColor(textColor);
                        head.setText(header.toString());
                        head.setTextSize(headerSize);
                        TextView mes = new TextView(this);
                        mes.setTextColor(textColor);
                        mes.setText(body.toString());
                        mes.setTextSize(bodySize);

                        newNotifHolder.addView(head);
                        newNotifHolder.addView(mes);
                        newNotifHolder.addView(acceptBtn);
                        newNotifHolder.addView(declineBtn);
                        notifContainer.addView(newNotifHolder);
                    }
                });
            }
        });


        WalkNotificationBuilder walk = new WalkNotificationBuilder()
                .walkTitle("Example Walk")
                .date("Example Date")
                .isCreator(true)
                .fromName("TeamMate")
                .result(true);

        Notification example = walk.getNotification();
//        addWalkElement(example);
 //       subscribeToNotificationsTopic();
    }

    private void subscribeToNotificationsTopic() {
        String currUserID = AccessSharedPrefs.getUserID(NotificationScreen.this);

   /*     FirebaseMessaging.getInstance().subscribeToTopic(currUserID)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(NotificationScreen.this, msg, Toast.LENGTH_SHORT).show();
                        }
                );

    */
    }



    protected void addWalkElement(WalkNotification notif){
        int textColor = Color.parseColor("#FFFFFFFF");
        int bodySize = 20;
        int headerSize = 30;
        Drawable draw = getDrawable(R.drawable.rounded_edges);
        LinearLayout notifContainer = findViewById(R.id.notif_container);
        LinearLayout newNotifHolder = new LinearLayout(this);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        newNotifHolder.setOrientation(LinearLayout.VERTICAL);
        newNotifHolder.setBackground(draw);
        newNotifHolder.setLayoutParams(containerParams);

        TextView header = new TextView(this);
        header.setTextColor(textColor);
        header.setText(notif.getWalkTitle());
        header.setTextSize(headerSize);
        TextView body = new TextView(this);
        body.setTextColor(textColor);
        body.setText(notif.getFromName());
        body.setTextSize(bodySize);


        newNotifHolder.addView(header);
        newNotifHolder.addView(body);
        notifContainer.addView(newNotifHolder);
    }

    private void initMessageUpdateListener() {
        chat.addSnapshotListener((newChatSnapShot, error) -> {
            if (error != null) {
                Log.e(TAG, error.getLocalizedMessage());
                Log.d("Notification: ", "There is an error subscribing");
                return;
            }

            if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {

                Log.d("Notification: ", "SOMETHING RETURNED! GOOD!");
                StringBuilder sb = new StringBuilder();
                List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
                documentChanges.forEach(change -> {
                    QueryDocumentSnapshot document = change.getDocument();
                    sb.append(document.get("fromUserID"));
                    sb.append(":\n");
                    sb.append(document.get("teamId"));
                    sb.append("\n");
                    sb.append("---\n");
                });


                LinearLayout chatView = findViewById(R.id.notif_container);
                TextView t = new TextView(this);
                t.setText(sb.toString());
                int textColor = Color.parseColor("#FFFFFFFF");
                t.setTextColor(textColor);
                chatView.addView(t);
            }
        });
    }

}
