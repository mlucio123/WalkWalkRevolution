package com.example.cse110_project;

import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;
import android.util.Log;



public class GoogleMapNavigation {

    private final String URL = "https://www.google.com/maps/search/?api=1&query=";

    private String startPosition;
    private String query;
    private String TAG = "Google** Map: ";

    /*Empty Constructor for unit tests only*/
    GoogleMapNavigation() {

    }

    GoogleMapNavigation(String startPosition) {
        this.startPosition = startPosition;
    }

    GoogleMapNavigation(TextView startPosition) {
        this.startPosition = startPosition.getText().toString();
    }

    public Intent getURL() {
        Log.i(TAG, this.startPosition);

        this.query = URL +  startPosition.replace(" ", "+");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(query));
        return browserIntent;
    }

    public String getQuery() {
        return query;
    }

    public String getUrlPrefix() {
        return URL;
    }

    public String getStartPosition() {
        return startPosition;
    }


}
