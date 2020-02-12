
package com.example.cse110_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.cse110_project.fitness.FitnessServiceFactory;
import com.example.cse110_project.fitness.FitnessService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WalkScreen extends AppCompatActivity {

    private String fitnessServiceKey = "GOOGLE_FIT";
    private static final long FIVE_SECS = 5000;
    private static final String TAG = "xxWALK SCREEN: ";

    private Button startButton;
    private Button doneWalkButton;
    private Button endButton;
    private Chronometer mChronometer;
    private BottomNavigationView bottomNavigationView;
    private Button boostTimeBtn;
    private long walkTime;
    private long addedWalkTime;
    private long startTime;

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final int FEET_IN_MILE = 5280;
    private TextView textSteps;
    private FitnessService fitnessService;

    private boolean walking;
    private boolean testing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk_screen);

        /*
         * Create and start fitnessService
         */
        fitnessService = FitnessServiceFactory.create(this, false);
        fitnessService.setup();

        startButton = findViewById(R.id.startWalkMaterial);
        endButton = findViewById(R.id.stopWalkMaterial);
        doneWalkButton = findViewById(R.id.doneWalkBtn);
        mChronometer = findViewById(R.id.timerDisplay);
        boostTimeBtn = findViewById(R.id.boostBtn);
        endButton.setVisibility(View.GONE);

        if(AccessSharedPrefs.getWalkStartTime(WalkScreen.this) != -1) {
            walking = true;
            Log.d(TAG, "YOU SAVED A TIME");
            setOnWalkUI();
            startTime = AccessSharedPrefs.getWalkStartTime(WalkScreen.this);
            Log.d(TAG, "Using start time: " + String.valueOf(startTime));
            mChronometer.setBase(startTime);
            mChronometer.start();
            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                    setChronoText(time);
                }
            });
            Log.d(TAG, "AFTER START");
        }

        textSteps = findViewById(R.id.stepView);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: test cross screen fitness service
                fitnessService.listActiveSubscriptions();

                setOnWalkUI();

                //toggle walk status, and save
                walking = true;

                startTime = SystemClock.elapsedRealtime();
                mChronometer.setBase(startTime);
                AccessSharedPrefs.setWalkStartTime(WalkScreen.this, startTime);
                Log.d(TAG, "after saved time");

                mChronometer.start();
                setChronoText(mChronometer.getBase() - SystemClock.elapsedRealtime());
                mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                        setChronoText(time);
                    }
                });

            }
        });

        doneWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(walking) AccessSharedPrefs.setWalkStartTime(WalkScreen.this, startTime);
                Intent intent = new Intent(WalkScreen.this, RouteScreen.class);
                startActivity(intent);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walking = false;
                AccessSharedPrefs.setWalkStartTime(WalkScreen.this, -1);
                walkTime = SystemClock.elapsedRealtime() - mChronometer.getBase();
                Log.d("Walktime is: ", String.valueOf(walkTime));
                startButton.setVisibility(View.VISIBLE);
                endButton.setVisibility(View.GONE);
                mChronometer.stop();
                mChronometer.setEnabled(false);
                Intent intent = new Intent(WalkScreen.this, RouteFormScreen.class);
                startActivity(intent);
            }
        });

        boostTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(walking) {
                    startTime-=FIVE_SECS;
                    mChronometer.setBase(mChronometer.getBase() - FIVE_SECS);
                    setChronoText(SystemClock.elapsedRealtime() - mChronometer.getBase());
                    return;
                }
                long elapsedRealTimeOffset = System.currentTimeMillis() - SystemClock.elapsedRealtime();
                mChronometer.setBase((addedWalkTime - elapsedRealTimeOffset));
                setChronoText(mChronometer.getBase());
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

    public void setChronoText(long newTime) {
        int h = (int) (newTime / 3600000);
        int m = (int) (newTime - h * 3600000) / 60000;
        int s = (int) (newTime - h * 3600000 - m * 60000) / 1000;
        String hStr = h < 10 ? "0" + h : h + "";
        String mStr = m < 10 ? "0" + m : m + "";
        String sStr = s < 10 ? "0" + s : s + "";
        String format = hStr + ":" + mStr + ":" + sStr;
        mChronometer.setText(format);
    }

    private void setOnWalkUI() {
        //toggle button visibility
        startButton.setVisibility(View.GONE);
        endButton.setVisibility(View.VISIBLE);
    }

    private void selectFragment(MenuItem item){

        Intent newIntent = new Intent(this, this.getClass());
        switch(item.getItemId()) {
            case R.id.navigation_home:
                AccessSharedPrefs.setWalkStartTime(WalkScreen.this, startTime);
                newIntent = new Intent(this, HomeScreen.class);
                newIntent.putExtra(HomeScreen.FITNESS_SERVICE_KEY, fitnessServiceKey);
                startActivity(newIntent);
                break;
            case R.id.navigation_routes:
                AccessSharedPrefs.setWalkStartTime(WalkScreen.this, startTime);
                newIntent = new Intent(this, RouteScreen.class);
                startActivity(newIntent);
                break;
            default:
                break;
        }
    }
}
