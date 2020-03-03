package com.example.cse110_project.notifications;

public class InviteNotification implements Notification {
    private Notification.NotifType type;
    private String fromName;
    private String teamID;
    private boolean accepted;

    public InviteNotification( String fromName, String teamID,  boolean accepted) {
        this.type = NotifType.InviteNotification;
        this.fromName = fromName;
        this.accepted = accepted;
        this.teamID = teamID;
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
    public String getMessage(){
        String[] returnString = {};
        return returnString.toString();
    }
}
