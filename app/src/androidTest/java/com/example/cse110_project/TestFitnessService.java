package com.example.cse110_project;

import com.example.cse110_project.fitness.FitnessService;

public class TestFitnessService implements FitnessService {

    private long currentStepCount = 1000;

    private static final String TAG = "[TestFitnessService]: ";
    private HomeScreen homeScreen;

    public TestFitnessService(HomeScreen stepCountActivity) {
        this.homeScreen = stepCountActivity;
    }

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    public void readHistoryData() {

    }

    @Override
    public void setup() {
        System.out.println(TAG + "setup");
    }

    @Override
    public void updateStepCount() {
        System.out.println(TAG + "updateStepCount");
        homeScreen.setStepCount(currentStepCount);
    }

    public void setStepCount(long newStepCount ) {
        this.currentStepCount = newStepCount;
    }
}
