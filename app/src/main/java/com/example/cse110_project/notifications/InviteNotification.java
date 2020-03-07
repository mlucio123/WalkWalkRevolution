package com.example.cse110_project.notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InviteNotification implements Notification {
    private Notification.NotifType type;
    private String fromName;
    private String teamID;
    private String deviceID;
    private boolean accepted;
    FirebaseFirestore db;

    private String TAG = "INVITATION NOTIFICATION : ";

    public InviteNotification( String deviceID, String fromName, String teamID) {
        this.type = NotifType.InviteNotification;
        this.fromName = fromName;
        this.teamID = teamID;
        this.deviceID = deviceID;
        db = FirebaseFirestore.getInstance();
    }


    @Override
    public Notification.NotifType getType(){
        return type;
    }

    @Override
    public String getFromName(){
        return this.fromName;
    }



    public void addUserToTeamList(String deviceID, String teamID) {

        Map<String, Object> mp = new HashMap<>();
        mp.put("DUMB", "FIELDS");

        db.collection("teams")
                .document(teamID)
                .update("listOfUserIDs", FieldValue.arrayUnion(deviceID))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully append " + deviceID + " to " + teamID + "'s list.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }


    //@Override
    public void deleteNotification(String device, String fromDeviceID, String teamID, String elID){
        db.collection("teams")
                .document(teamID)
                .collection("listOfPendingUserIds")
                .whereEqualTo("userID", device).addSnapshotListener((newSnapShot, err) -> {
            for(DocumentSnapshot doc : newSnapShot.getDocuments()) {
                doc.getReference().delete();
            }
        });


        db.collection("users")
                .document(device)
                .collection("invitations")
                .document(elID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully deleted " + device + " from " + elID + "'s list.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void acceptInvitation(String teamID, String deviceID){


    }
}
