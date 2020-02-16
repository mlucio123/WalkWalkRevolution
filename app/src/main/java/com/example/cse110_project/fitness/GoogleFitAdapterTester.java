package com.example.cse110_project.fitness;

import android.app.Activity;

public class GoogleFitAdapterTester implements FitnessService {
    private Activity activity;

    private static int dailySteps;
    private static int dailyDistance;

    private static int currentWalkSteps;
    private static int currentWalkDistance;


    public GoogleFitAdapterTester(Activity activity, boolean is_test) {
        this.activity = activity;
    }

    @Override
    public void setup(){

    }

    @Override
    public void listActiveSubscriptions(){

    }

    @Override
    public void startRecording(){

    }

    @Override
    public int getRequestCode(){
        return 0;
    }

    @Override
    public long getDailySteps(){
        return dailySteps;
    }

    @Override
    public long getDailyDistance(){
        return dailyDistance;
    }


    public static void incrementDailySteps() {
        dailySteps += 500;
    }

    public static void incrementDailyDistance(long distance) {
        dailyDistance += distance;
    }

    public static void incrementCurrentWalkSteps() {
        currentWalkSteps += 500;
    }

    public static void incrementDailyDistance(int distance) {
        dailyDistance += distance;
    }

    @Override
    public void listSensorSubscriptions(){}

}
