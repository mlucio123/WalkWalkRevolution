package com.example.cse110_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeScreen extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Button startWalkBtn;
    private Button addRouteBtn;
    private Chronometer mChronometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return false;
            }
        });


        mChronometer = findViewById(R.id.homeTimer);
        mChronometer.setVisibility(View.INVISIBLE);

        startWalkBtn = (Button) findViewById(R.id.startBtn);

        startWalkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchThing();
                System.out.println("BEFORE CHECK");
                if(getIntent().hasExtra("START_TIME")) {
                    long base = getIntent().getLongExtra("START_TIME", 0L);
                    startTimer(base);

                }
                System.out.println("AFTER CHECK");
            }
        });

//        startWalkBtn = (Button) findViewById(R.id.startBtn);
//        startWalkBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomeScreen.this, WalkScreen.class);
//                startActivity(intent);
//            }
//        });

        addRouteBtn = (Button) findViewById(R.id.addBtn);
        addRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, RouteScreen.class);
                startActivity(intent);
            }
        });

    }

    private void selectFragment(MenuItem item){

        Intent newIntent = new Intent(this, this.getClass());
        switch(item.getItemId()) {
            case R.id.navigation_home:
                newIntent = new Intent(this, HomeScreen.class);
                break;
            case R.id.navigation_routes:
                newIntent = new Intent(this, RouteScreen.class);
                startActivity(newIntent);
                break;
            case R.id.navigation_walk:
                newIntent = new Intent(this, WalkScreen.class);
                startActivity(newIntent);
                break;
            default:
                break;
        }
    }

    public void launchThing() {
        Intent intent = new Intent(this, WalkScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void startTimer(long base) {
        System.out.println("STARTING TIMER");
        mChronometer.setVisibility(View.VISIBLE);
        mChronometer.setBase(base);
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
    }
}
