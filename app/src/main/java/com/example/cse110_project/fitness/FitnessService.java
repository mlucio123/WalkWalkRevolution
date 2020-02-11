package com.example.cse110_project.fitness;


import com.google.android.gms.common.data.DataBufferObserver;

public interface FitnessService {
    int getRequestCode();
    void setup();
    void updateStepCount();
    void readHistoryData();
}
