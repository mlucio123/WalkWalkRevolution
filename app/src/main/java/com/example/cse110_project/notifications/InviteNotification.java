package com.example.cse110_project.notifications;

import com.google.firebase.firestore.FirebaseFirestore;

public class InviteNotification implements Notification {
    private Notification.NotifType type;
    private String fromName;
    private String teamID;
    private boolean accepted;
    FirebaseFirestore db;


    public InviteNotification( String fromName, String teamID,  boolean accepted) {
        this.type = NotifType.InviteNotification;
        this.fromName = fromName;
        this.accepted = accepted;
        this.teamID = teamID;
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

    @Override
    public String getWalkTitle() {
        return "Title";
    }

    @Override
    public String getMessage(){
        String[] returnString = {};
        return returnString.toString();
    }

    public void deleteNotification(String deviceID, String inviteID){
        db.collection("users").document(deviceID).collection("invitations").document(inviteID).delete();
    }
}
