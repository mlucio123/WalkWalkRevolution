package com.example.cse110_project;

import android.util.Log;

import com.google.firebase.FirebaseApp;
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
    private boolean favorite;

    private boolean out, loop;
    private boolean flat, hills;
    private boolean even, rough;
    private boolean street, trail;
    private boolean easy, medium, hard;

    private final String TAG = "ROUTE CLASS: ";

    //other stuff

    public Route( String name, String startingPoint ){
        this.name = name;
        this.startingPoint = startingPoint;
    }

    public Route( String name, String startingPoint, boolean[] tags, boolean favorite, String extraNotes) {
            this.name = name;
            this.startingPoint = startingPoint;
            this.tags = tags;
            this.extraNotes = extraNotes;
            this.favorite = favorite;
    }

    public void setId(String id){ this.id = id; }

    public String getName(){
        return this.name;
    }

    public String getStartingPoint(){
        return this.startingPoint;
    }


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

        Log.d(TAG, "Submit below information to firebase");
        for(String key : route.keySet()){
            Log.d(TAG, key + " : " + route.get(key));
        }
        return route;
    }
}
