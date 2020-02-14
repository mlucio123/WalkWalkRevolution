package com.example.cse110_project.fitness;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.util.Log;
import java.util.*;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Field;

public class GoogleFitAdapter implements FitnessService {
    /* Member variables */
    private Activity activity;
    private GoogleSignInAccount account;
    private FitnessOptions fitnessOptions;
    private long steps;
    private long distance;

    /* Static variables */
    // TODO: might have problem(**)
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private final String TAG = "GoogleFitAdapter";


    /**
     * Method: GoogleFitAdapter (Activity activity, boolean is_test)
     * Description: GoogleFitAdapter Constructor
     *
     * @param activity
     * @param is_test
     */
    public GoogleFitAdapter(Activity activity, boolean is_test) {
        this.activity = activity;
    }


    /**
     * Method: setup()
     * Description: setup fitnessService by setting fitnessOptions,
     * sign in account, and check account permissions
     */
    @Override
    public void setup() {
        // Create fitness Option object (add data type e.g. step count)
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        // Sign in account
        account = GoogleSignIn.getAccountForExtension(activity, fitnessOptions);

        // Check Account Permissions
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    activity,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(activity),
                    fitnessOptions);
        } else {
//            list active subscriptions
            Log.i(TAG, "SETUP IS WORKING");
            listActiveSubscriptions();
        }
    }


    /**
     * Method:getRequestCode()
     * Description: get request code, check request_OK flag
     */
    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }


    /**
     * Method:startRecording()
     * Description: subscribe to record step count and distance
     */
    @Override
    public void startRecording() {
        if (account == null) {
            return;
        }

        // Subscribe to STEP_COUNT data
        Fitness.getRecordingClient(activity, account)
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully subscribed to Step Count!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "There was a problem subscribing to Step Count.");
                    }
                });

        // Subscribe to DISTANCE_CUMULATIVE data
        Fitness.getRecordingClient(activity, account)
                .subscribe(DataType.TYPE_DISTANCE_CUMULATIVE)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully subscribed to Distance!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "There was a problem subscribing to distance.", e);
                    }
                });
        Log.i(TAG, "START RECORDING IS WORKING");
        listActiveSubscriptions();
    }


    /**
     * Method:listActiveSubscriptions()
     * Description: return a list of subscriptions (step_count and distance_cummulative)
     */
    @Override
    public void listActiveSubscriptions() {

        // List STEP_COUNT active subs
        Fitness.getRecordingClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .listSubscriptions()
                .addOnSuccessListener(new OnSuccessListener<List<Subscription>>() {
                    @Override
                    public void onSuccess(List<Subscription> subscriptions) {
                        for (Subscription sc : subscriptions) {
                            String dataType = sc.getDataType().getName();
                            String debugString =  sc.toDebugString();
                            String logMsg = String.format("Active subscription: [DataType]: %s; [DebugString]: %s", dataType, debugString);
                            Log.i(TAG, logMsg);
                        }
                    }
                });

    }


    /**
     * Method: getDailySteps()
     * Description: Return current daily steps. This is a public method
     * that should be called in other activities.
     */
    @Override
    public long getDailySteps(){
        getGoogleDailySteps();
        return this.steps;
    }


    /**
     * Method: getDailyDistance()
     * Description: Return current daily distance. This is a public method
     * that should be called in other activities.
     */
    @Override
    public long getDailyDistance(){
        getGoogleDailyDistance();
        return this.distance;
    }



    /* Helper Functions */
    /**
     * Method: setDailySteps()
     * Description: Update member variable this.step.
     */
    private void setDailySteps(long steps){
        Log.d(TAG, "Total Daily Steps: " + steps);
        this.steps = steps;
    }


    /**
     * Method: getGoogleDailySteps()
     * Description: Get daily steps from google api.
     */
    private void getGoogleDailySteps(){
        Fitness.getHistoryClient(activity, account)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                Log.d(TAG, "STEP_COUNT_DATASET: " + dataSet.toString());
                                long total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();

                                setDailySteps(total);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }


    /**
     * Method: setDailyDistance()
     * Description: Update member variable this.distance.
     */
    private void setDailyDistance(long distance){
        Log.d(TAG, "Total Daily Distance: " + distance);
        this.distance = distance;
    }


    /**
     * Method: getGoogleDailyDistance()
     * Description: Get daily distance from google api.
     */
    private void getGoogleDailyDistance(){
        Fitness.getHistoryClient(activity, account)
                .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
                .addOnSuccessListener(
                        new OnSuccessListener<DataSet>() {
                            @Override
                            public void onSuccess(DataSet dataSet) {
                                Log.d(TAG, "DISTANCE_DELTA_DATASET: " + dataSet.toString());
                                long total =
                                        dataSet.isEmpty()
                                                ? 0
                                                : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();

                                setDailyDistance(total);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "There was a problem getting the step count.", e);
                            }
                        });
    }
}
