package com.example.cse110_project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cse110_project.notifications.Notification;
import com.example.cse110_project.notifications.WalkNotification;
import com.example.cse110_project.notifications.WalkNotificationBuilder;
import com.example.cse110_project.utils.AccessSharedPrefs;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

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

        String currUserID = AccessSharedPrefs.getUserID(NotificationScreen.this);
        Log.d("Notification: ", "This is user's id " + currUserID);
        chat = FirebaseFirestore.getInstance()
                .collection("users")
                .document(currUserID)
                .collection("invitations");
        initMessageUpdateListener();


        WalkNotificationBuilder walk = new WalkNotificationBuilder()
                .walkTitle("Example Walk")
                .date("Example Date")
                .isCreator(true)
                .fromName("TeamMate")
                .result(true);

        Notification example = walk.getNotification();
//        addWalkElement(example);
        subscribeToNotificationsTopic();
    }

    private void subscribeToNotificationsTopic() {
        String currUserID = AccessSharedPrefs.getUserID(NotificationScreen.this);

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



    protected void addWalkElement(Notification notif){
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
                    sb.append(document.get("from"));
                    sb.append(":\n");
                    sb.append(document.get("text"));
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
