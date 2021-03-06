package com.example.cse110_project.notifications;

public class WalkNotification implements Notification {

    private Notification.NotifType type;
    private boolean isCreator;
    private String fromName;
    private String walkTitle;
    private String date;
    private String result;

    public WalkNotification(boolean creator, String fromName, String walkTitle, String date, String result) {
        this.isCreator = creator;
        this.type = Notification.NotifType.WalkNotification;
        this.fromName = fromName;
        this.walkTitle = walkTitle;
        this.date = date;
        this.result = result;
    }


    @Override
    public Notification.NotifType getType(){
        return type;
    }


    public String getWalkTitle(){
        return this.walkTitle;
    }

    @Override
    public String getFromName(){
        return this.fromName;
    }

    @Override
    public String getResult() {return this.result;}

    @Override
    public boolean getIsCreator(){return this.isCreator;}


    public String getMessage(){
        String[] returnString = {};
        return returnString.toString();
    }

    //@Override
    public void deleteNotification(String deviceID, String from, String notifID){

    }
}
