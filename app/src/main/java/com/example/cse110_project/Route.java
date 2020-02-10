package com.example.cse110_project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Observable;

public class Route extends Observable {

    private String name;
    private String startingPoint;
    private boolean[] tags;
    private String extraNotes;
    private Boolean favorite;
    private DatabaseReference mDatabase;

    //other stuff

    public Route( String name, String startingPoint, boolean[] tags, boolean favorite, String extraNotes) {
            this.name = name;
            this.startingPoint = startingPoint;
            this.tags = tags;
            this.extraNotes = extraNotes;
            this.favorite = favorite;
    }

    public String getName(){
        return this.name;
    }

    public String getStartingPoint(){
        return this.startingPoint;
    }


    public void post(){
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
       // DatabaseReference myRef = database.getReference("routes");

       // myRef.setValue("Hello, World!");
    }
}
