
package com.example.cse110_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WalkScreen extends AppCompatActivity {

    private String fitnessServiceKey = "GOOGLE_FIT";
    private static final long FIVE_SECS = 5000;

    private Button startButton;
    private Button doneWalkButton;
    private Button endButton;
    private Chronometer mChronometer;
    private BottomNavigationView bottomNavigationView;
    private Button boostTimeBtn;
    private long walkTime;
    private long addedWalkTime;

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "HomeScreen";
    private static final int FEET_IN_MILE = 5280;
    private TextView textSteps;
    private com.example.cse110_project.fitness.FitnessService fitnessService;

    private boolean walking;
    private boolean testing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk_screen);

        startButton = findViewById(R.id.startWalkMaterial);
        endButton = findViewById(R.id.stopWalkMaterial);
        doneWalkButton = findViewById(R.id.doneWalkBtn);
        mChronometer = findViewById(R.id.timerDisplay);
        boostTimeBtn = findViewById(R.id.boostBtn);
        endButton.setVisibility(View.GONE);

        textSteps = findViewById(R.id.stepView);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walking = true;
                startButton.setVisibility(View.GONE);
                endButton.setVisibility(View.VISIBLE);
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
                setChronoText(mChronometer.getBase() - SystemClock.elapsedRealtime());
                mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        if(!walking) {
                            return;
                        }
                        long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                        setChronoText(time);
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
                Intent intent = new Intent(WalkScreen.this, RouteFormScreen.class);
startActivity(intent);
            }
        });

        boostTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(walking) {
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
