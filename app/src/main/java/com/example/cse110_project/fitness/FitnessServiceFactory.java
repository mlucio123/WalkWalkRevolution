package com.example.cse110_project.fitness;

import android.app.Activity;

public class FitnessServiceFactory {
    public static FitnessService create(Activity activity, boolean is_test){

        // Check is_test flag, return testFitnessService if flag is on
        if (is_test) {
            System.out.println("IS TEST");
            return new GoogleFitAdapterTester(activity, is_test);
        }
        else {
            return new GoogleFitAdapter(activity, is_test);
        }
    }
}
