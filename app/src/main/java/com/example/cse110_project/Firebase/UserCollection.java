package com.example.cse110_project.Firebase;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cse110_project.utils.Route;
import com.example.cse110_project.utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserCollection {

    FirebaseFirestore db;
    private final String TAG = "Firebase User";
    private String teamID;
    private boolean gettingTeamID = true;


    /* Initialize firebase instance */
    public UserCollection() {
        db = FirebaseFirestore.getInstance();
        if (this.db == null) {
            Log.d(TAG, "Unsuccessful Firebase instance");
        } else {
            Log.d(TAG, "Success");
        }
    }


    /* Initialize Firebase App */
    public static void initFirebase(Context context) {
        FirebaseApp.initializeApp(context);
    }


    /* add user along with device ID */
    public void addUser(User newUser, String deviceID) {

        Map<String, Object> newUserInfo = newUser.getUserFieldsMap();


        // Add a new document with a generated ID
        db.collection("users")
            .document(deviceID)
            .set(newUserInfo)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing document", e);
                }
            });
    }


    public void getUserID(String deviceID) {

        DocumentReference docRef = db.collection("users").document(deviceID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }



    /* Get current user for current device */
    public void getUser(String deviceID){
        DocumentReference docRef = db.collection("users").document(deviceID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    /* Get list of teammate names*/
    public void getTeammatesList(String deviceID, final TeammatesListListener myListener) {
        Log.d(TAG, "DEVICE ID: " + deviceID);
        DocumentReference userRef = db.collection("users").document(deviceID);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Object teamIDObj = document.get("teamID");

                        // TODO: handle no teammate case
                        if (teamIDObj == null){
                            Log.d(TAG, "TEAM ID: NOT FOUND");
                            return;
                        }
                        else {
                            String teamID = teamIDObj.toString();
                            Log.d(TAG, "TEAM ID: " + teamID);
                            DocumentReference teamRef = db.collection("teams").document(teamID);
                            teamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            List<String> userIds = (List<String>) document.get("listOfUserIDs");
                                            Log.i(TAG, "FOUND LIST OF USERS" + userIds.toString());

                                            for (int i = 0; i < userIds.size(); i++) {
                                                String userID = userIds.get(i);
                                                DocumentReference currentUserRef = db.collection(("users")).document(userID);
                                                currentUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                String firstName = document.get("firstName").toString();
                                                                String lastName = document.get("lastName").toString();
                                                                String userName = firstName + " " + lastName;
                                                                Log.i(TAG, "CURRENT USERNAME: " + userName);
                                                                myListener.onSuccess(userName);
                                                            }

                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            });
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    /* Get pending teammates */
    public void getPendingTeammatesList(String deviceID, final TeammatesListListener myListener){
        Log.i(TAG, "Device ID is: " + deviceID);
        DocumentReference userRef = db.collection("users").document(deviceID);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        Object teamIDObj = document.get("teamID");
                        if (teamIDObj == null){
                            Log.d(TAG, "teamID NOT FOUND");
                            return;
                        }
                        else{
                            String teamID = document.get("teamID").toString();
                            Log.d(TAG, "DocumentSnapshot data: " + teamID);
                            db.collection("teams")
                                    .document(teamID)
                                    .collection("listOfPendingUserIds")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    if (document.exists()){
                                                        String userID = document.get("userID").toString();
                                                        Log.d(TAG, userID);
                                                        DocumentReference currentUserRef = db.collection(("users")).document(userID);
                                                        currentUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    DocumentSnapshot document = task.getResult();
                                                                    if (document.exists()) {
                                                                        String firstName = document.get("firstName").toString();
                                                                        String lastName = document.get("lastName").toString();
                                                                        String userName = firstName + " " + lastName;
                                                                        Log.i(TAG, "CURRENT USERNAME: " + userName);
                                                                        myListener.onSuccess(userName);
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }

                                                }

                                            } else {
                                                Log.w(TAG, "Error getting documents.", task.getException());
                                            }
                                        }
                                    });

                        }
                    }
                    else {
                        Log.d(TAG, "No such document");
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


//    public void setTeamID(String teamID){
//        this.teamID = teamID;
//        gettingTeamID = false;
//    }
//
//    public String getTeamID(String deviceID){
//        getTeamIdFromFirebase(deviceID);
//        while(gettingTeamID);
////        gettingTeamID = true;
//        return this.teamID;
//    }

}
