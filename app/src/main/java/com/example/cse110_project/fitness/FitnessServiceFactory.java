package com.example.cse110_project.fitness;

import android.util.Log;

import com.example.cse110_project.HomeScreen;

import java.util.HashMap;
import java.util.Map;

public class FitnessServiceFactory {

    private static final String TAG = "[FitnessServiceFactory]";

    private static Map<String, BluePrint> blueprints = new HashMap<>();

    public static void put(String key, BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static FitnessService create(String key, HomeScreen homeScreen) {
        Log.i(TAG, String.format("creating FitnessService with key %s", key));
        return blueprints.get(key).create(homeScreen);
    }

    public interface BluePrint {
        FitnessService create(HomeScreen stepCountActivity);
    }
}
