package com.example.cse110_project.fitness;


public interface FitnessService {
    void setup();
    void listActiveSubscriptions();
    void startRecording();
    long getDailySteps();
    int getRequestCode();
//    void updateStepCount();
//    void readHistoryData();
}
