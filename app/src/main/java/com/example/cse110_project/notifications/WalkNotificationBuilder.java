package com.example.cse110_project.notifications;

public class WalkNotificationBuilder extends AbstractNotificationBuilder {
    private boolean creator;
    private String walkTitleString;
    private String fromNameString;
    private String  date;
    private boolean res;

    public WalkNotificationBuilder walkTitle(String walkTitleString){
        this.walkTitleString = walkTitleString;
        return this;
    }
    public WalkNotificationBuilder fromName(String fromNameString){
        this.fromNameString = fromNameString;
        return this;
    }
    public WalkNotificationBuilder isCreator(boolean creator){
        this.creator = creator;
        return this;
    }
    public WalkNotificationBuilder date(String date){
        this.date = date;
        return this;
    }
    public WalkNotificationBuilder result(boolean res){
        this.res = res;
        return this;
    }

    @Override
    public WalkNotification getNotification() {
        return new WalkNotification(this.creator, this.fromNameString, this.walkTitleString, this.date, this.res);
    }
}
