package com.example.cse110_project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse110_project.Firebase.FirebaseMessageService;
import com.example.cse110_project.notifications.InviteNotification;
import com.example.cse110_project.notifications.WalkNotification;
import com.example.cse110_project.utils.AccessSharedPrefs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class NotificationScreen extends AppCompatActivity {
    String TAG = NotificationScreen.class.getSimpleName();
    ImageButton cancel;
    CollectionReference chat;
    FirebaseMessageService service;
    String messagingToken;
    String currUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_screen);
        cancel = (ImageButton) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        String currentDeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token

                        String token = task.getResult().getToken();

                        messagingToken = token;
                        // Log and toast
                        //String msg = getString('%', token);
                        Log.d(TAG, token);
                        // Toast.makeText(NotificationScreen.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        currUserID = AccessSharedPrefs.getUserID(NotificationScreen.this);
        subscribeToNotificationsTopic();

        Log.d("Notification: ", "This is user's id " + currUserID);
        chat = FirebaseFirestore.getInstance().collection("users");

        // Create a query against the collection.
        Query query = chat.whereEqualTo("deviceID", currUserID).orderBy("timestamp", Query.Direction.ASCENDING);
        int textColor = Color.parseColor("#FFFFFFFF");
        int bodySize = 20;
        int headerSize = 30;

        //retrieve  query results asynchronously using query.get()
        query.addSnapshotListener((newChatSnapShot, error) -> {
            for (DocumentSnapshot document : newChatSnapShot.getDocuments()) {
                document.getReference().collection("invitations")
                        .get()
                        .addOnSuccessListener((newSnapShot) -> {
                            List<DocumentChange> documentChanges = newSnapShot.getDocumentChanges();
                            documentChanges.forEach(change -> {
                                QueryDocumentSnapshot doc = change.getDocument();
                                chat.document(doc.get("fromUserID").toString()).addSnapshotListener((snapShot, err) -> {
                                    String name = snapShot.get("firstName").toString();
                                    StringBuilder header = new StringBuilder();
                                    StringBuilder body = new StringBuilder();
                                    header.append("You have a team invite from ");
                                    header.append(name);
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
                                    InviteNotification i = new InviteNotification(currUserID, doc.get("fromUserID").toString(),
                                            doc.get("teamId").toString(), currUserID.equals(doc.get("fromUserID").toString()), "");
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
                                            inviteNotification.deleteNotification(currUserID, doc.getId(), doc.get("teamId").toString(), doc.getId(), "declined");
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
                                            inviteNotification.deleteNotification(currUserID, doc.getId().toString(), doc.get("teamId").toString(), doc.getId(), "accepted");
                                            newNotifHolder.setVisibility(View.GONE);
                                            AccessSharedPrefs.saveOnTeam(NotificationScreen.this);

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
                                });
                            });
                        });
            }


//        addWalkElement(example);
            //       subscribeToNotificationsTopic();
        });
    }


    private void subscribeToNotificationsTopic() {


        FirebaseMessaging.getInstance().subscribeToTopic(currUserID)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(NotificationScreen.this, msg, Toast.LENGTH_SHORT).show();
                        }
                );


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
