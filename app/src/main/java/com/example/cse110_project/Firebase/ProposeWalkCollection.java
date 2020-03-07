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

        infoMap.put("Dummy", "info");

        db.collection("teams")
                .document(teamID)
                .collection("proposeWalk")
                .add(infoMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
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


    public void getTeamID(String deviceID, Map<String, Object> infoMap) {

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
                                    proposeWalkToTeam(teamID, infoMap);
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



}
