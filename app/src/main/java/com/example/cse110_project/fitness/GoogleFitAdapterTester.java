package com.example.cse110_project.fitness;
import com.example.cse110_project.AccessSharedPrefs;


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
        if( AccessSharedPrefs.getDailyStepsTester(activity) == -1 || AccessSharedPrefs.getDailyDistanceTester(activity) == -1) {
            AccessSharedPrefs.saveDailyStatsTester(activity, 0, 0);
        }

        dailyDistance = AccessSharedPrefs.getDailyDistanceTester(activity);
        dailySteps = AccessSharedPrefs.getDailyStepsTester(activity);
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
        dailySteps = AccessSharedPrefs.getDailyStepsTester(activity);
        return dailySteps;
    }

    @Override
    public long getDailyDistance(){
        dailyDistance = AccessSharedPrefs.getDailyDistanceTester(activity);
        return dailyDistance;
    }


    public void incrementDailySteps() {
        dailyDistance = AccessSharedPrefs.getDailyDistanceTester(activity);
        dailySteps = AccessSharedPrefs.getDailyStepsTester(activity);
        dailySteps += 500;
        AccessSharedPrefs.saveDailyStatsTester(activity, dailySteps, dailyDistance);

    }

    public void incrementDailyDistance(int distance) {

        dailyDistance = AccessSharedPrefs.getDailyDistanceTester(activity);
        dailySteps = AccessSharedPrefs.getDailyStepsTester(activity);
        dailyDistance += distance;
        AccessSharedPrefs.saveDailyStatsTester(activity, dailySteps, dailyDistance);
    }

    public static void incrementCurrentWalkSteps() {
        currentWalkSteps += 500;
    }


    @Override
    public void listSensorSubscriptions(){}

}
