package com.example.cse110_project.notifications;

public interface Notification {
    public enum NotifType
    {
        WalkNotification,
        InviteNotification

    }
    String getWalkTitle();
    String getFromName();
    NotifType getType();
    String getMessage();

}
