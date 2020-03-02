package com.example.cse110_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GoogleMapNavigation extends AppCompatActivity {

    protected final String URL = "https://www.google.com/maps/search/?api=1&query=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_screen);

        String latitude = "47.5951518";
        String longitude = "-122.3316393";

        String Query = URL + latitude + "," + longitude;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Query));
        //Alternative approach that Directly start Google Maps rather than browser
        /*Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");*/

        startActivity(browserIntent);

    }

    protected String getGPSCoordinate(Object scheduleWalk) {
        // TODO : need to Implement scheduleWalk and gain corresponding information from there
        return null;
    }


}
