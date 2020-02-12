
package com.example.cse110_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cse110_project.fitness.FitnessServiceFactory;
import com.example.cse110_project.fitness.FitnessService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WalkScreen extends AppCompatActivity {

    private String fitnessServiceKey = "GOOGLE_FIT";

    private Button startButton;
    private Button doneWalkButton;
    private Button endButton;
    private Chronometer mChronometer;
    private BottomNavigationView bottomNavigationView;
    private long walkTime;

    private TextView routeTitle;
    private TextView routeStart;
    private TextView routeNotes;
    private TextView routeSummaryTitle;

    private LinearLayout routeLastCompletedTimeLayout;
    private LinearLayout routeLastCompletedStepsLayout;
    private LinearLayout routeLastCompletedDistanceLayout;

    private TextView routeLastCompletedTime;
    private TextView routeLastcompletedSteps;
    private TextView routeLastcompletedDistance;

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "HomeScreen";
    private static final int FEET_IN_MILE = 5280;
    private TextView textSteps;
    private TextView textDistance;
    private FitnessService fitnessService;

    private boolean walking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk_screen);

        /**
         * Create and start fitnessService
         */
        fitnessService = FitnessServiceFactory.create(this, false);
        fitnessService.setup();

        startButton = findViewById(R.id.startWalkMaterial);
        endButton = findViewById(R.id.stopWalkMaterial);
        doneWalkButton = findViewById(R.id.doneWalkBtn);
        mChronometer = findViewById(R.id.timerDisplay);
        endButton.setVisibility(View.GONE);

        textSteps = findViewById(R.id.stepView);
        textDistance = findViewById(R.id.distanceView);

        routeLastCompletedTimeLayout = findViewById(R.id.routeCompletedTimeLayout);
        routeLastCompletedStepsLayout = findViewById(R.id.routeCompletedStepsLayout);
        routeLastCompletedDistanceLayout = findViewById(R.id.routeCompletedDistanceLayout);

        routeLastCompletedTime = findViewById(R.id.routeCompletedTime);
        routeLastcompletedSteps = findViewById(R.id.routeCompletedSteps);
        routeLastcompletedDistance = findViewById(R.id.routeCompletedDistance);

        routeStart = findViewById(R.id.routeStartWalkScreen);
        routeTitle = findViewById(R.id.routeTitleWalkScreen);
        routeNotes = findViewById(R.id.routeNotesWalkScreen);
        routeSummaryTitle = findViewById(R.id.route_summary_title);

        routeNotes.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        String title = intent.getStringExtra("routeName");
        String start = intent.getStringExtra("routeStart");
        String notes = intent.getStringExtra("routeNotes");

        String lastTime = intent.getStringExtra("lastCompletedTime");
        String lastSteps = intent.getStringExtra( "lastCompletedSteps");
        String lastDistance = intent.getStringExtra("lastCompletedDistance");

        LinearLayout layout = findViewById(R.id.route_summary);

        Log.d("WALKSCREEN", "THIS IS " +  lastTime);
        Log.d("WALKSCREEN", "THIS IS " + lastSteps);
        Log.d("WALKSCREEN", "THIS IS " + lastDistance);


        if(title == null && start == null && notes == null){
            routeSummaryTitle.setText("This is a new route!");
        } else {
            routeSummaryTitle.setText("Route Summary");
            layout.setVisibility(View.VISIBLE);
            routeTitle.setText(title);
            routeStart.setText(start);
            routeNotes.setText(notes);
            if(lastTime != null || lastTime.length() != 0){
                routeLastCompletedTimeLayout.setVisibility(View.VISIBLE);
                routeLastCompletedTime.setText(lastTime);
            } else {
                routeLastCompletedTimeLayout.setVisibility(View.GONE);
            }

            if(lastSteps != null || lastSteps.length() != 0){
                routeLastCompletedStepsLayout.setVisibility(View.VISIBLE);
                routeLastcompletedSteps.setText(lastSteps);
            } else {
                routeLastCompletedStepsLayout.setVisibility(View.GONE);
            }

            if(lastDistance != null || lastDistance.length() != 0){
                routeLastCompletedDistanceLayout.setVisibility(View.VISIBLE);
                routeLastcompletedDistance.setText(lastDistance);
            } else {
                routeLastCompletedDistanceLayout.setVisibility(View.GONE);
            }
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: test cross screen fitness service
                fitnessService.listActiveSubscriptions();

                walking = true;
                startButton.setVisibility(View.GONE);
                endButton.setVisibility(View.VISIBLE);
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
                mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        if(!walking) {
                            return;
                        }
                        long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                        int h = (int) (time / 3600000);
                        int m = (int) (time - h * 3600000) / 60000;
                        int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                        String hStr = h < 10 ? "0" + h : h + "";
                        String mStr = m < 10 ? "0" + m : m + "";
                        String sStr = s < 10 ? "0" + s : s + "";
                        String format = hStr + ":" + mStr + ":" + sStr;
                        chronometer.setText(format);
                    }
                });
                //start updating steps/distance traveled

            }
        });

        doneWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walking = false;
                Intent intent = new Intent(WalkScreen.this, RouteScreen.class);
                startActivity(intent);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walking = false;
                walkTime = SystemClock.elapsedRealtime() - mChronometer.getBase();
                startButton.setVisibility(View.VISIBLE);
                endButton.setVisibility(View.GONE);
                mChronometer.stop();
                mChronometer.setEnabled(false);

                String timer = mChronometer.getText().toString();
                String steps = textSteps.getText().toString();
                String distance = textDistance.getText().toString();

                Intent intent = new Intent(WalkScreen.this, RouteFormScreen.class);
                intent.putExtra("completedTime", timer);
                intent.putExtra("stepCount", steps);
                intent.putExtra("distance", distance);
                startActivity(intent);
            }
        });

        /* Bottom Navigation Bar */
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return false;
            }
        });

    }

    private void selectFragment(MenuItem item){

        Intent newIntent = new Intent(this, this.getClass());
        switch(item.getItemId()) {
            case R.id.navigation_home:
                newIntent = new Intent(this, HomeScreen.class);
                newIntent.putExtra(HomeScreen.FITNESS_SERVICE_KEY, fitnessServiceKey);
                startActivity(newIntent);
                break;
            case R.id.navigation_routes:
                newIntent = new Intent(this, RouteScreen.class);
                startActivity(newIntent);
                break;
            default:
                break;
        }
    }
}
