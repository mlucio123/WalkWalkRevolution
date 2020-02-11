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

    /* Static variables */
    // TODO: might have problem(**)
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private final String TAG = "GoogleFitAdapter";


    /**
     * Method: GoogleFitAdapter (Activity activity, boolean is_test)
     *
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
     *
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
            listActiveSubscriptions();
        }

    }


    /**
     * Method:getRequestCode()
     *
     * Description: get request code, check request_OK flag
     */
    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }


    /**
     * Method:listActiveSubscriptions()
     *
     * Description: return a list of subscriptions (step_count and distance_cummulative)
     */
    @Override
    public void listActiveSubscriptions() {

        // List STEP_COUNT active subs
        Fitness.getRecordingClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .listSubscriptions(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener(new OnSuccessListener<List<Subscription>>() {
                    @Override
                    public void onSuccess(List<Subscription> subscriptions) {
                        for (Subscription sc : subscriptions) {
                            DataType dt = sc.getDataType();
                            Log.i(TAG, "Active subscription for data type: " + dt.getName());
                        }
                    }
                });

        // List STEP_COUNT_CUMMULATIVE active subs
        Fitness.getRecordingClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
                .listSubscriptions(DataType.TYPE_DISTANCE_CUMULATIVE)
                .addOnSuccessListener(new OnSuccessListener<List<Subscription>>() {
                    @Override
                    public void onSuccess(List<Subscription> subscriptions) {
                        for (Subscription sc : subscriptions) {
                            DataType dt = sc.getDataType();
                            Log.i(TAG, "Active subscription for data type: " + dt.getName());
                        }
                    }
                });
    }


    /**
     * Method:startRecording()
     *
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
    }
}
