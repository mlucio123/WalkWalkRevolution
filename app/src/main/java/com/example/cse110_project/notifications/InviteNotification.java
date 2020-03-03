package com.example.cse110_project.notifications;

public class InviteNotification implements Notification {
    private Notification.NotifType type;
    private boolean isCreator;
    private String fromName;
    private String walkTitle;
    private String date;
    private boolean accepted;

    public InviteNotification(boolean creator, String fromName, String walkTitle, String date, boolean accepted) {
        this.isCreator = creator;
        this.type = Notification.NotifType.WalkNotification;
        this.fromName = fromName;
        this.walkTitle = walkTitle;
        this.date = date;
        this.accepted = accepted;
    }


    @Override
    public Notification.NotifType getType(){
        return type;
    }
    @Override
    public String getWalkTitle(){
        return this.walkTitle;
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
