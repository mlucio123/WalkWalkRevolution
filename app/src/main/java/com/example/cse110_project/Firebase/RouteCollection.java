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
                                try {
                                    Route newRoute = makeRoute(document);
                                    qryRoutes.add(makeRoute(document));
                                    routesSimpleList.add(makeRoute(document));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d(TAG, "THIS ROUTE CAN't be ADDED");
                                }

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

            String title = "";

            if(qry.getData().get("title") != null) {
                title = qry.getData().get("title").toString();
            }

            String start_position = "";

            if(qry.getData().get("start_position") != null){
                start_position = qry.getData().get("start_position").toString();
            }


            String notes = "";

            if(qry.getData().get("notes") != null){
                notes = qry.getData().get("notes").toString();
            }


            boolean out = false;
            boolean loop= false;
            boolean flat= false;
            boolean hills= false;
            boolean even= false;
            boolean rough= false;
            boolean street= false;
            boolean trail= false;
            boolean easy= false;
            boolean medium= false;
            boolean hard= false;
            boolean favorite= false;

            if(qry.getData().get("out") != null){
                out = Boolean.parseBoolean(qry.getData().get("out").toString());
            }
            if(qry.getData().get("loop") != null){
                loop = Boolean.parseBoolean(qry.getData().get("loop").toString());
            }
            if(qry.getData().get("flat") != null){
                flat = Boolean.parseBoolean(qry.getData().get("flat").toString());
            }
            if(qry.getData().get("hills") != null){
                hills = Boolean.parseBoolean(qry.getData().get("hills").toString());
            }
            if(qry.getData().get("even") != null){
                even  = Boolean.parseBoolean(qry.getData().get("even").toString());
            }
            if(qry.getData().get("rough") != null){
                rough = Boolean.parseBoolean(qry.getData().get("rough").toString());
            }
            if(qry.getData().get("street") != null){
                street = Boolean.parseBoolean(qry.getData().get("street").toString());
            }
            if(qry.getData().get("trail") != null){
                trail = Boolean.parseBoolean(qry.getData().get("trail").toString());
            }
            if(qry.getData().get("easy") != null){
                easy = Boolean.parseBoolean(qry.getData().get("easy").toString());
            }
            if(qry.getData().get("medium") != null){
                medium = Boolean.parseBoolean(qry.getData().get("medium").toString());
            }
            if(qry.getData().get("hard") != null){
                hard = Boolean.parseBoolean(qry.getData().get("hard").toString());
            }
            if(qry.getData().get("favorite") != null){
                favorite = Boolean.parseBoolean(qry.getData().get("favorite").toString());
            }

            boolean[] tags ={out, loop, flat, hills, even, rough, street, trail, easy, medium, hard, favorite};
            newRoute = new Route(title, start_position, tags, notes);


           // newRoute = new Route(title, start_position);
            newRoute.setNotes(notes);
            newRoute.setId(qry.getId().toString());

            String time;
            String steps;
            String distance;

            if(qry.getData().get("lastCompletedTime") != null ){
                time = qry.getData().get("lastCompletedTime").toString();
                if(time != null){
                    newRoute.setLastCompletedTime(time);
                }
            }

            if(qry.getData().get("lastCompletedSteps") != null){
                steps = qry.getData().get("lastCompletedSteps").toString();
                if(steps != null){
                    newRoute.setLastCompletedSteps(steps);
                }
            }

            if(qry.getData().get("lastCompletedDistance") != null){
                distance = qry.getData().get("lastCompletedDistance").toString();
                if(distance != null){
                    newRoute.setLastCompletedDistance(distance);
                }
            }


            return newRoute;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "PROBLEMS WITH GETTING BACK ROUTES");
            return null;
        }

    }


    /* TODO: Update firebase with newest time */

    /* TODO: Add to firebase with extra information */




}
