package com.example.cse110_project.Firebase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cse110_project.utils.Route;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProposeWalkCollection {

    FirebaseFirestore db;
    private final String TAG = "FirebaseRoutes";
    public ArrayList<Route> qryRoutes;
    ArrayList<QueryDocumentSnapshot> qryDocs;

    /* Initialize firebase instance */
    public ProposeWalkCollection() {
        db = FirebaseFirestore.getInstance();
        if (this.db == null) {
            Log.d(TAG, "Unsuccessful firebase instance");
        } else {
            Log.d(TAG, "Success");
        }
        this.qryRoutes = new ArrayList<Route>();
    }


    /* Initialize Firebase App */
    public static void initFirebase(Context context) {
        FirebaseApp.initializeApp(context);
    }



    public void proposeWalkToTeam(String teamID, Map<String, Object> infoMap){

//        infoMap.put("Dummy", "info");

        db.collection("teams")
                .document(teamID)
                .collection("proposeWalk")
                .document(teamID)
                .set(infoMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Propose a walk to " + teamID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error proposing a walk document", e);
                    }
                });


    }


    public void getTeamID(String deviceID, Map<String, Object> infoMap, String rule) {

        db.collection("users")
                .document(deviceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "FOUND Device'S TeamID: " + document.get("teamID"));

                                if(document.getData().get("teamID") != null) {
                                    String teamID = document.get("teamID").toString();

                                    if(rule.equals("propose")) {
                                        proposeWalkToTeam(teamID, infoMap);
                                    } else if(rule.equals("getResponse")) {
                                        getResponseCount(teamID);
                                    }


                                }


                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error at getTeamRoutesFromDevice" , e);
                    }
                });

    }


    public void withdrawWalk(String deviceID) {
        // TODO : delete the proposed walk

        db.collection("users")
                .document(deviceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot rootDoc = task.getResult();


                        String returnedTeamID = "";

                        if(rootDoc.getData().get("teamID") != null ) {
                            returnedTeamID = rootDoc.getData().get("teamID").toString();


                            db.collection("teams")
                                    .document(returnedTeamID)
                                    .collection("proposeWalk")
                                    .document(returnedTeamID)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Updated proposed walk to the user." );
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error at getTeamRoutesFromDevice", e);
                    }
                });

    }


    public void setScheduled(String deviceID) {

        db.collection("users")
                .document(deviceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot rootDoc = task.getResult();


                        String returnedTeamID = "";

                        if(rootDoc.getData().get("teamID") != null ) {
                            returnedTeamID = rootDoc.getData().get("teamID").toString();

                            Map<String, Object> mp = new HashMap<>();
                            mp.put("isScheduled", true);

                            db.collection("teams")
                                    .document(returnedTeamID)
                                    .collection("proposeWalk")
                                    .document(returnedTeamID)
                                    .update(mp)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Updated proposed walk to the user." );
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error at getTeamRoutesFromDevice", e);
                    }
                });

    }

    public void getResponseCount(String teamID) {

        db.collection("teams")
                .document(teamID)
                .collection("proposeWalk")
                .document(teamID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                // TODO : GET ACCEPTED / DECLINED COUNTS
//                                List<String> accept = Arrays.asList(a);

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error at getTeamRoutesFromDevice" , e);
                    }
                });


    }






}
