package com.example.cse110_project;

import java.util.Observable;

public class Route extends Observable {

    private String name;
    private String startingPoint;
    private boolean[] tags;
    private String extraNotes;
    private Boolean favorite;

    //other stuff

    public Route( String name, String startingPoint, boolean[] tags, boolean favorite, String extraNotes) {
            this.name = name;
            this.startingPoint = startingPoint;
            this.tags = tags;
            this.extraNotes = extraNotes;
            this.favorite = favorite;
    }
}
