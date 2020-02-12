package com.example.cse110_project.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.cse110_project.Route;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteCollection {

    FirebaseFirestore db;
    private final String TAG = "FirebaseRoutes";
    ArrayList<Route> qryRoutes;
    ArrayList<QueryDocumentSnapshot> qryDocs;

    /* Initialize firebase instance */
    public RouteCollection() {
        db = FirebaseFirestore.getInstance();
        if (this.db == null) {
            Log.d(TAG, "Unsuccessful firebase instance");
        } else {
            Log.d(TAG, "Success");
        }
        this.qryRoutes = new ArrayList<Route>();
    }


    /* add routes along with device ID */
    public void addRoute(Route addRoute, String deviceID) {

        Map<String, Object> route = addRoute.getFeatureMap();
        route.put("deviceID", deviceID);

         // Add a new document with a generated ID
        db.collection("routes")
                .add(route)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    /* Get routes for current device */
    public void getRoutes(String deviceID, final MyCallback myCallback){

        db.collection("routes")
                .whereEqualTo("deviceID", deviceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Route> routesSimpleList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                qryRoutes.add(makeRoute(document));
                                routesSimpleList.add(makeRoute(document));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            myCallback.getRoutes(routesSimpleList);
                            Log.d(TAG, "CURRENT LIST OF ROUTES: " + qryRoutes.size());

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        myCallback.getRoutes(qryRoutes);
//                    }
//                });
    }


    /* This method makes Route object from returning query document snapshot */
    private Route makeRoute(QueryDocumentSnapshot qry){

        try {
            Route newRoute;
            String id = qry.getId();
            String title = qry.getData().get("start_position").toString();
            String start_position = qry.getData().get("title").toString();
            String notes = qry.getData().get("notes").toString();
            newRoute = new Route(title, start_position);
            newRoute.setNotes(notes);
            newRoute.setId(qry.getId().toString());
            return newRoute;

        } catch (Exception e) {
            Log.d(TAG, "PROBLEMS WITH GETTING BACK ROUTES");
            return null;
        }

    }


    /* TODO: Update firebase with newest time */

    /* TODO: Add to firebase with extra information */




}
