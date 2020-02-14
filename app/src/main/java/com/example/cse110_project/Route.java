package com.example.cse110_project;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.FirebaseApp;
/*
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;
*/
import android.util.Log;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.core.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class Route extends Observable {

    private String id;

    private String name;
    private String startingPoint;
    private boolean[] tags;
    private String extraNotes;
    private boolean favorite1;
    private boolean favorite;

    private boolean out, loop;
    private boolean flat, hills;
    private boolean even, rough;
    private boolean street, trail;
    private boolean easy, medium, hard;

    private String lastCompletedTime;
    private String lastCompletedSteps;
    private String lastCompletedDistance;

    private final String TAG = "ROUTE CLASS: ";

    //other stuff

    public Route( String name, String startingPoint ){
        this.name = name;
        this.startingPoint = startingPoint;
        this.lastCompletedTime = this.lastCompletedSteps = this.lastCompletedDistance = "";
    }

    public Route( String name, String startingPoint, boolean[] tags,  String extraNotes) {
            this.name = name;
            this.startingPoint = startingPoint;
            this.tags = tags;
            this.extraNotes = extraNotes;
    }

    public void setId(String id){ this.id = id; }

    public String getName(){
        return this.name;
    }

    public String getStartingPoint(){
        return this.startingPoint;
    }

    public boolean[] getTags() { return this.tags; }

    public boolean getFavorite(){ return this.favorite; }


    public String getNotes() { return this.extraNotes; }

    public void setTags(boolean[] tags){
        out = tags[0];
        loop = tags[1];
        flat = tags[2];
        hills = tags[3];
        even = tags[4];
        rough = tags[5];
        street = tags[6];
        trail = tags[7];
        easy = tags[8];
        medium = tags[9];
        hard = tags[10];
        favorite = tags[11];
    }

    public void setNotes(String notes){
        this.extraNotes = notes;
    }

    public String getLastCompletedTime() { return this.lastCompletedTime; }

    public void setLastCompletedTime(String time) { this.lastCompletedTime = time; }

    public String getLastCompletedSteps() { return this.lastCompletedSteps; }

    public void setLastCompletedSteps(String steps) { this.lastCompletedSteps = steps; }

    public String getLastCompletedDistance() { return this.lastCompletedDistance; }

    public void setLastCompletedDistance(String distance) { this.lastCompletedDistance = distance; }

    public HashMap<String, Object> getFeatureMap () {
        HashMap<String, Object> route = new HashMap<>();
        route.put("title", this.getName());
        route.put("start_position", this.getStartingPoint());
        route.put("out", out);
        route.put("loop", out);
        route.put("flat", flat);
        route.put("hills", hills);
        route.put("even", even);
        route.put("rough", rough);
        route.put("street", street);
        route.put("trail", trail);
        route.put("easy", easy);
        route.put("medium", medium);
        route.put("hard", hard);
        route.put("favorite", favorite);
        route.put("notes", extraNotes);
        route.put("lastCompletedTime", lastCompletedTime);
        route.put("lastCompletedSteps", lastCompletedSteps);
        route.put("lastCompletedDistance", lastCompletedDistance);

        Log.d(TAG, "Submit below information to firebase");
        for(String key : route.keySet()){
            Log.d(TAG, key + " : " + route.get(key));
        }
        return route;
    }
}
