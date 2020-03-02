package com.example.cse110_project.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class User {

    String deviceID;
    String gmail;
    String firstName;
    String lastName;
    String initial;
    String teamID;
    String color;
    String heightFt;
    String heightInch;


    public User(String deviceID, String gmail, String firstName, String lastName) {
        this.deviceID = deviceID;
        this.gmail = gmail;
        this.firstName = firstName;
        this.lastName = lastName;

        if(firstName.length() == 0 && lastName.length() == 0){
            this.setInitial("?");
        } else if (firstName.length() == 0 && lastName.length() != 0) {
            this.setInitial(lastName.substring(0, 1));
        } else if (firstName.length() != 0 && lastName.length() == 0) {
            this.setInitial(firstName.substring(0, 1));
        } else {
            String init = firstName.substring(0, 1) + lastName.substring(0, 1);
            this.setInitial(init);
        }
    }

    public Map<String, Object> getUserFieldsMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Random rnd = new Random();

        int r = rnd.nextInt(256);
        int g = rnd.nextInt(256);
        int b = rnd.nextInt(256);

        map.put("deviceID", this.getDeviceID());
        map.put("gmail", this.getGmail());
        map.put("firstName", this.getFirstName());
        map.put("lastName", this.getLastName());
        map.put("color_r", r);
        map.put("color_g", g);
        map.put("color_b", b);

        if (teamID != null && teamID.length() != 0) {
            map.put("teamID", this.getTeamID());
        }

        map.put("initial", this.getInitial());
        map.put("ft", this.getHeightFt());
        map.put("inch", this.getHeightInch());

        return map;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getGmail() {
        return gmail;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getHeightFt() {
        return heightFt;
    }

    public void setHeightFt(String heightFt) {
        this.heightFt = heightFt;
    }

    public String getHeightInch() {
        return heightInch;
    }

    public void setHeightInch(String heightInch) {
        this.heightInch = heightInch;
    }

}
