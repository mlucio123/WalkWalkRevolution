package com.example.cse110_project.fitness;


public interface FitnessService {
    void setup();
    void listActiveSubscriptions();
    void startRecording();
    long getDailySteps();
    long getDailyDistance();
    int getRequestCode();
    void incrementDailySteps();
    void incrementDailyDistance(int distance);
    void listSensorSubscriptions();

//    void updateStepCount();
//    void readHistoryData();
}
