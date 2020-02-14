package com.example.cse110_project.fitness;


public interface FitnessService {
    void setup();
    void listActiveSubscriptions();
    void startRecording();
    int getRequestCode();
    void readHistory();
//    void updateStepCount();
//    void readHistoryData();
}
