
package com.example.cse110_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Observable;

public class WalkScreen extends AppCompatActivity {

    private String fitnessServiceKey = "GOOGLE_FIT";

    private Button startButton;
    private Button endButton;
    private Chronometer mChronometer;
    private BottomNavigationView bottomNavigationView;
    private long walkTime;
    Walk currentWalk;

    private boolean walking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk_screen);

        startButton = findViewById(R.id.startWalkMaterial);
        endButton = findViewById(R.id.stopWalkMaterial);
        mChronometer = findViewById(R.id.timerDisplay);
        endButton.setVisibility(View.GONE);
//        Button homeButton = findViewById(R.id.homeButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walking = true;
                startButton.setVisibility(View.GONE);
                endButton.setVisibility(View.VISIBLE);
                mChronometer.setBase(SystemClock.elapsedRealtime());
                currentWalk = new Walk(mChronometer.getBase());
                mChronometer.start();
                mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
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

//        homeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save walk + stats, show prompt
                walkTime = SystemClock.elapsedRealtime() - mChronometer.getBase();
                currentWalk.setEndTime(walkTime);
                startButton.setVisibility(View.VISIBLE);
                endButton.setVisibility(View.GONE);
                mChronometer.stop();
                mChronometer.setEnabled(false);
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

    /*public void onHomePressed() {
        new AlertDialog.Builder(this)
                .setTitle("Ending Walk")
                .setMessage("Are you sure you want to end your current walk")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }*/
}
