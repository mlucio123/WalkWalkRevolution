package com.example.cse110_project;

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

    public HashMap<String, Object> getFeatureMap () {
        HashMap<String, Object> route = new HashMap<>();
        route.put("title", this.getName());
        route.put("start_position", this.getStartingPoint());
        return route;
    }
}
