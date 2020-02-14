package com.example.cse110_project.fitness;

import android.app.Activity;

public class GoogleFitAdapterTester implements FitnessService {
    private Activity activity;

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
        return 0;
    }

}
