package com.example.cse110_project;

import java.util.Observable;

public class Walk extends Observable {

    private double startTime;
    private double endtime;
    private String startingPoint;
    private int steps;
    private double distance;
    //other stuff

    public Walk(long startTime) {
        this.startTime = startTime;
    }

    void setSteps(int newSteps) { this.steps = newSteps; }

    void setEndTime(double endTime ) { this.endtime = endTime; }
}