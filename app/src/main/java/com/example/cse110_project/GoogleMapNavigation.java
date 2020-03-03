package com.example.cse110_project;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;


public class GoogleMapNavigation {

    protected final String URL = "https://www.google.com/maps/search/?api=1&query=";

    protected TextView startDisplay;

    public GoogleMapNavigation(TextView startDisplay) {
        this.startDisplay = startDisplay;
    }

    public Intent getURL() {

        final String URL = "https://www.google.com/maps/search/?api=1&query=";
        String name = startDisplay.getText().toString();
        name.replace(" ", "+");
        String Query = URL +  name;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Query));

        return browserIntent;

    }



}
