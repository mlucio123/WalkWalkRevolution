package com.example.cse110_project.notifications;

public interface Notification {
    public enum NotifType
    {
        WalkNotification,
        InviteNotification

    }

    String getFromName();
    String getWalkTitle();
    NotifType getType();
    String getMessage();

}
