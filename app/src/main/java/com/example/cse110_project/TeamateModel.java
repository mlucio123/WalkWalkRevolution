package com.example.cse110_project;

public class TeamateModel {

    public static final int ACCEPT_TYPE=0;
    public static final int PENDING_TYPE=1;

    public int type;
    public String text;

    public TeamateModel(int type, String text)
    {
        this.type=type;
        this.text = text;
    }
}