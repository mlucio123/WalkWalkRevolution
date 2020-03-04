package com.example.cse110_project.notifications;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InviteNotification implements Notification {
    private Notification.NotifType type;
    private String fromName;
    private String teamID;
    private String deviceID;
    private boolean accepted;
    FirebaseFirestore db;


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
                .document(fromDeviceID)
                .collection("invitations")
                .document(elID)
                .delete();
    }

    public void acceptInvitation(String teamID, String deviceID){


    }
}
