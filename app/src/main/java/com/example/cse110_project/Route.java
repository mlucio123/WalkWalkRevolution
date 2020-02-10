package com.example.cse110_project;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("routes").push();

        myRef.child("new").setValue("Hello, World!");

    }
}
