package com.example.cse110_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cse110_project.Firebase.RouteCollection;

public class RouteFormScreen extends AppCompatActivity {

    private Button favBtn;
    private Button easyBtn;
    private Button moderateBtn;
    private Button hardBtn;
    private Button cancelBtn;
    private Button submitBtn;
    private Button rStyleLoop;
    private Button rStyleOut;
    private Button rLandFlat;
    private Button rLandHills;
    private Button rTypeStreet;
    private Button rTypeTrail;
    private Button rSurfaceEven;
    private Button rSurfaceRough;
    private boolean loop;
    private boolean out;
    private boolean flat;
    private boolean hills;
    private boolean street;
    private boolean trail;
    private boolean even;
    private boolean rough;
    private boolean easy;
    private boolean medium;
    private boolean hard;
    private boolean favorite;
    public Route newRoute; // Public for now for testing

    private EditText routeName;
    private EditText startPosition;
    private EditText notes;

    private String fitnessServiceKey = "GOOGLE_FIT";
    private String TAG = "ROUTE FORM: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_form);

        routeName = findViewById(R.id.routeName);
        startPosition = findViewById(R.id.routeStart);
        notes = findViewById(R.id.notesText);

        cancelBtn = findViewById(R.id.cancelBtn);
        submitBtn = findViewById(R.id.submitBtn);

        easyBtn = findViewById(R.id.easyBtn);
        moderateBtn = findViewById(R.id.moderateBtn);
        hardBtn = findViewById(R.id.hardBtn);

        favBtn = findViewById(R.id.favBtn);

        rStyleLoop = findViewById(R.id.routeStyleLoop);
        rStyleOut = findViewById(R.id.routeStyleOutAndBack);
        rLandFlat = findViewById(R.id.routeLandFlat);
        rLandHills = findViewById(R.id.routeLandHills);
        rTypeStreet = findViewById(R.id.routeTypeStreets);
        rTypeTrail = findViewById(R.id.routeTypeTrail);
        rSurfaceEven = findViewById(R.id.surfaceEven);
        rSurfaceRough = findViewById(R.id.surfaceRough);


        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO : FIX LOGIC AND SEND FAV TO BACKEND OR ROUTE OBJECT
                // TODO : THIS CURRENTLY REMOVES THE BORDER IN THE FAVORITE BUTTON

                if ( favBtn.getBackgroundTintList() != ColorStateList.valueOf( 0xFFFF0000 )){
                    favBtn.setBackgroundTintList( ColorStateList.valueOf( 0xFFFF0000 ) );
                    favorite = true;
                } else {
                    favBtn.setBackgroundTintList( ColorStateList.valueOf( 0xFF000000 ) );
                    favorite = false;
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // return to walk screen
                finish();
            }
        });


        rStyleLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] bools = {loop, out};
                switchType(rStyleLoop, rStyleOut, bools);
                loop = bools[0];
                out = bools[1];
            }
        });

        rStyleOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] bools = {out, loop};
                switchType(rStyleOut, rStyleLoop, bools);
                out = bools[0];
                loop = bools[1];
            }
        });

        rLandFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] bools = {flat, hills};
                switchType(rLandFlat, rLandHills, bools);
                flat = bools[0];
                hills = bools[1];
            }
        });

        rLandHills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] bools = {hills, flat};
                switchType(rLandHills, rLandFlat, bools);
                hills = bools[0];
                flat = bools[1];
            }
        });

        rSurfaceEven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] bools = {even, rough};
                switchType(rSurfaceEven, rSurfaceRough, bools);
                even = bools[0];
                rough = bools[1];
            }
        });

        rSurfaceRough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] bools = {rough, even};
                switchType(rSurfaceRough, rSurfaceEven, bools);
                rough = bools[0];
                even = bools[1];
            }
        });

        rTypeStreet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] bools = {street, trail};
                switchType(rTypeStreet, rTypeTrail, bools);
                street = bools[0];
                trail = bools[1];
            }
        });

        rTypeTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] bools = {trail, street};
                switchType(rTypeTrail, rTypeStreet, bools);
                trail = bools[0];
                street = bools[1];
            }
        });


        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] bools = {easy, medium, hard};
                switchType3(easyBtn, moderateBtn, hardBtn, bools);
                easy = bools[0];
                medium = bools[1];
                hard = bools[2];
            }
        });

        moderateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] bools = {medium, easy, hard};
                switchType3(moderateBtn, easyBtn, hardBtn, bools);
                medium = bools[0];
                easy = bools[1];
                hard = bools[2];
            }
        });

        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] bools = {hard, easy, medium};
                switchType3(hardBtn, easyBtn, moderateBtn, bools);
                hard = bools[0];
                easy = bools[1];
                medium = bools[2];
            }
        });



        /* Submit on click listener */
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (routeName.getText().toString().equals("")) {
                    Toast.makeText(RouteFormScreen.this, "You did not fill in route name.", Toast.LENGTH_SHORT).show();
                } else {

                    // TODO CREATE OBJ OF CORRESPONDING MESSAGES AND SEND TO FIREBASE
                    boolean[] tags ={out, loop, flat, hills, even, rough, street, trail, easy, medium, hard, favorite};

                    Log.d(TAG, "REsult : " + out + flat + hills + even + rough + street + trail + easy + medium + hard);

                    String timer = getIntent().getStringExtra("completedTime");
                    String steps = getIntent().getStringExtra("stepCount");
                    String distance = getIntent().getStringExtra("distance");

                    newRoute = new Route(routeName.getText().toString(), startPosition.getText().toString(),
                            tags, favorite, "");
                    newRoute.setTags(tags);
                    newRoute.setNotes(notes.getText().toString());

                    if (timer != null || timer.length() != 0){
                        newRoute.setLastCompletedTime(timer);
                    }

                    if (steps != null || steps.length() != 0){
                        newRoute.setLastCompletedSteps(steps);
                    }

                    if (distance != null || distance.length() != 0){
                        newRoute.setLastCompletedDistance(distance);
                    }

                    RouteCollection rc = new RouteCollection();
                    String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    rc.addRoute(newRoute, deviceID);

                    Toast.makeText(RouteFormScreen.this, "Form Submitted!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RouteFormScreen.this, RouteScreen.class);
                    intent.putExtra(HomeScreen.FITNESS_SERVICE_KEY, fitnessServiceKey);
                    startActivity(intent);
                }
            }
        });
    }

    public void switchType(Button target, Button parallel, boolean[] bools){
        int white = Color.parseColor("#FFFFFFFF");
        int black = Color.parseColor("#FF000000");
        if (target.getCurrentTextColor() != black){
            target.setTextColor(black);
            Drawable drawable = getDrawable(R.drawable.btn_circular_selected);
            target.setBackground(drawable);
            bools[0] = true;
            if(bools[1]){
                bools[1] = false;
                parallel.setTextColor(white);
                Drawable draw = getDrawable(R.drawable.btn_circular);
                parallel.setBackground(draw);
            }
        } else{
            target.setTextColor(white);
            Drawable drawable = getDrawable(R.drawable.btn_circular);
            target.setBackground(drawable);
            bools[0] = false;
        }
    }

    public void switchType3(Button target, Button parallel1, Button parallel2, boolean[] bools){
        int white = Color.parseColor("#FFFFFFFF");
        int black = Color.parseColor("#FF000000");
        if (target.getCurrentTextColor() != black){
            target.setTextColor(black);
            Drawable drawable = getDrawable(R.drawable.btn_circular_selected);
            target.setBackground(drawable);
            bools[0] = true;
            if(bools[1] || bools[2]){
                bools[1] = false;
                bools[2] = false;
                Drawable draw = getDrawable(R.drawable.btn_circular);
                parallel1.setTextColor(white);
                parallel1.setBackground(draw);
                parallel2.setTextColor(white);
                parallel2.setBackground(draw);
            }
        } else{
            target.setTextColor(white);
            Drawable drawable = getDrawable(R.drawable.btn_circular);
            target.setBackground(drawable);
            bools[0] = false;
        }
    }
}
