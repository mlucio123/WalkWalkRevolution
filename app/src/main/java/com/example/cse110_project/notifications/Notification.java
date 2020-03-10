package com.example.cse110_project.notifications;

public interface Notification {
    public enum NotifType
    {
        WalkNotification,
        InviteNotification
    }
    String getFromName();
    NotifType getType();
    String getResult();
    boolean getIsCreator();
    //void deleteNotification(String deviceID, String fromDeviceID, String notificationID);
}
