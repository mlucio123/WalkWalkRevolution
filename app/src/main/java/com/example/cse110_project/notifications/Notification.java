package com.example.cse110_project.notifications;

public interface Notification {
    enum NotifType
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
