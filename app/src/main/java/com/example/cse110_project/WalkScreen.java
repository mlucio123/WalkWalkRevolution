
package com.example.cse110_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cse110_project.Firebase.RouteCollection;
import com.example.cse110_project.fitness.FitnessService;
import com.example.cse110_project.fitness.FitnessServiceFactory;
import com.example.cse110_project.utils.AccessSharedPrefs;
import com.example.cse110_project.utils.StrideCalculator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.BigDecimal;
import java.math.MathContext;

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
    private Button boostStepBtn;
    private long walkTime;
    private long addedWalkTime;
    private long startTime;

    private TextView routeTitle;
    private TextView routeStart;
    private TextView routeNotes;
    private TextView routeSummaryTitle;

    private LinearLayout routeLastCompletedTimeLayout;
    private LinearLayout routeLastCompletedStepsLayout;
    private LinearLayout routeLastCompletedDistanceLayout;

    private TextView routeLastCompletedTimeTitle;
    private TextView routeLastCompletedStepsTitle;
    private TextView routeLastcompletedDistanceTitle;

    private TextView routeLastCompletedTime;
    private TextView routeLastcompletedSteps;
    private TextView routeLastcompletedDistance;

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final Boolean USE_GOOGLE_FIT_TESTER = false;
    private static final int FEET_IN_MILE = 5280;
    public static boolean USE_TEST_SERVICE = false;
    private TextView textSteps;
    private TextView textDistance;
    private FitnessService fitnessService;

    public static boolean walking;
    private boolean is_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk_screen);

        /*
         * Create and start fitnessService
         */
        is_test = getIntent().getBooleanExtra("is_test", USE_TEST_SERVICE);
        fitnessService = FitnessServiceFactory.create(this, false);
        fitnessService.setup();

        startButton = findViewById(R.id.startWalkMaterial);
        endButton = findViewById(R.id.stopWalkMaterial);
        doneWalkButton = findViewById(R.id.doneWalkBtn);
        mChronometer = findViewById(R.id.timerDisplay);
        boostTimeBtn = findViewById(R.id.boostBtn);
        boostStepBtn = findViewById(R.id.boostStepBtn);

        endButton.setVisibility(View.GONE);


        if(AccessSharedPrefs.getWalkStartTime(WalkScreen.this) != -1 && !walking) {
            walking = true;
            setOnWalkUI();
            startTime = AccessSharedPrefs.getWalkStartTime(WalkScreen.this);
            Log.d(TAG, "SAVED TIME RETRIEVED: " + startTime);
            mChronometer.setBase(startTime);
            mChronometer.start();
            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                    setChronoText(time);
                }
            });
        }

        textSteps = findViewById(R.id.stepView);
        textDistance = findViewById(R.id.distanceView);

        routeLastCompletedTimeLayout = findViewById(R.id.routeCompletedTimeLayout);
        routeLastCompletedStepsLayout = findViewById(R.id.routeCompletedStepsLayout);
        routeLastCompletedDistanceLayout = findViewById(R.id.routeCompletedDistanceLayout);

        routeLastCompletedTimeTitle = findViewById(R.id.routeCompletedTimeTitle);
        routeLastCompletedStepsTitle = findViewById(R.id.routeCompletedStepsTitle);
        routeLastcompletedDistanceTitle = findViewById(R.id.routeCompletedDistanceTitle);

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
        String routeID = intent.getStringExtra("routeID");

        String lastTime = intent.getStringExtra("lastCompletedTime");
        String lastSteps = intent.getStringExtra( "lastCompletedSteps");
        String lastDistance = intent.getStringExtra("lastCompletedDistance");

        LinearLayout layout = findViewById(R.id.route_summary);

        Log.d(TAG, "THIS IS " +  lastTime);
        Log.d(TAG, "THIS IS " + lastSteps);
        Log.d(TAG, "THIS IS " + lastDistance);


        if(title == null && start == null && notes == null){
            routeSummaryTitle.setText("This is a new route!");
        } else {
            routeSummaryTitle.setText("Route Summary");
            layout.setVisibility(View.VISIBLE);
            routeTitle.setText(title);
            routeStart.setText(start);
            routeStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoogleMapNavigation googleMapNavigation = new GoogleMapNavigation(routeStart);
                    Intent browserIntent = googleMapNavigation.getURL();
                    startActivity(browserIntent);
                }
            });
            routeNotes.setText(notes);
            if(lastTime != null && lastTime.length() != 0){
                routeLastCompletedTimeLayout.setVisibility(View.VISIBLE);
                routeLastCompletedTimeTitle.setVisibility(View.VISIBLE);
                routeLastCompletedTime.setVisibility(View.VISIBLE);
                routeLastCompletedTime.setText(lastTime);
            } else {
                routeLastCompletedTimeLayout.setVisibility(View.GONE);
                routeLastCompletedTimeTitle.setVisibility(View.GONE);
                routeLastCompletedTime.setVisibility(View.GONE);
            }

            if(lastSteps != null && lastSteps.length() != 0){
                routeLastCompletedStepsLayout.setVisibility(View.VISIBLE);
                routeLastCompletedStepsTitle.setVisibility(View.VISIBLE);
                routeLastcompletedSteps.setVisibility(View.VISIBLE);
                routeLastcompletedSteps.setText(lastSteps);
            } else {
                routeLastCompletedStepsLayout.setVisibility(View.GONE);
                routeLastCompletedStepsTitle.setVisibility(View.GONE);
                routeLastcompletedSteps.setVisibility(View.GONE);
            }

            if(lastDistance != null && lastDistance.length() != 0){
                routeLastCompletedDistanceLayout.setVisibility(View.VISIBLE);
                routeLastcompletedDistanceTitle.setVisibility(View.VISIBLE);
                routeLastcompletedDistance.setVisibility(View.VISIBLE);
                routeLastcompletedDistance.setText(lastDistance);
            } else {
                routeLastCompletedDistanceLayout.setVisibility(View.GONE);
                routeLastcompletedDistanceTitle.setVisibility(View.GONE);
                routeLastcompletedDistance.setVisibility(View.GONE);
            }
        }

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
                Log.d(TAG, "SAVED START TIME: " + startTime);

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
                walking = false;
                AccessSharedPrefs.setWalkStartTime(WalkScreen.this, -1);
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
                Log.d(TAG, "Walktime is: " + walkTime);
                startButton.setVisibility(View.VISIBLE);
                endButton.setVisibility(View.GONE);
                mChronometer.stop();
                mChronometer.setEnabled(false);

                String timer = mChronometer.getText().toString();
                String steps = textSteps.getText().toString();
                String distance = textDistance.getText().toString();
                AccessSharedPrefs.saveWalk(WalkScreen.this, timer, steps, distance);

                if(routeID == null) {
                    Intent intent = new Intent(WalkScreen.this, RouteFormScreen.class);
                    intent.putExtra("completedTime", timer);
                    intent.putExtra("stepCount", steps);
                    intent.putExtra("distance", distance);
                    startActivity(intent);
                } else {
                    RouteCollection rc = new RouteCollection();
                    rc.updateRouteStats(routeID, timer, steps, distance);
                    Toast.makeText(WalkScreen.this, "You successfully updated your walk.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WalkScreen.this, RouteScreen.class);
                    startActivity(intent);
                }

            }
        });

        boostTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(walking) {
                    startTime-=FIVE_SECS;
                    mChronometer.setBase(mChronometer.getBase() - FIVE_SECS);
                    setChronoText(SystemClock.elapsedRealtime() - mChronometer.getBase());
                }
            }
        });

        boostStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walking){
                    String steps = textSteps.getText().toString();

                    int curr = Integer.parseInt(steps.substring(0,steps.indexOf(" ")));
                    setStepCount(curr + 500);
                    fitnessService.incrementDailySteps();
                }
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

    public void setStepCount(long stepCount) {
        textSteps.setText(String.valueOf(stepCount) + " Steps");

        // look in storage
        SharedPreferences sharedpreference_value = getSharedPreferences("user_info",MODE_PRIVATE);
        int heightFt = sharedpreference_value.getInt("heightFt", -1);
        int heightInch = sharedpreference_value.getInt("heightInch", -1);

        // calcualte stride length
        StrideCalculator calc = new StrideCalculator(heightFt, heightInch);
        double strideLength = calc.getStrideLength();
        double estimateDistance = stepCount * strideLength;

        BigDecimal bd = new BigDecimal(estimateDistance);
        bd = bd.round(new MathContext(3));
        double rounded = bd.doubleValue();


        double convert = (estimateDistance * 1.0 / FEET_IN_MILE );
        bd = new BigDecimal(convert);
        bd = bd.round(new MathContext(3));
        rounded = bd.doubleValue();
        rounded = Math.abs(rounded);
        String estDistStr = rounded + " Miles";
        textDistance.setText(estDistStr);
        if(is_test){
            fitnessService.incrementDailyDistance((int) rounded);
        }

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
                if(!walking) {
                    Log.d(TAG, "NOT SAVING TO HOME");
                    AccessSharedPrefs.setWalkStartTime(WalkScreen.this, -1);
                }
                newIntent = new Intent(this, HomeScreen.class);
                newIntent.putExtra(HomeScreen.FITNESS_SERVICE_KEY, fitnessServiceKey);
                startActivity(newIntent);
                break;
            case R.id.navigation_routes:
                if(!walking) {
                    Log.d(TAG, "NOT SAVING TO ROUTE");
                    AccessSharedPrefs.setWalkStartTime(WalkScreen.this, -1);
                }
                newIntent = new Intent(this, RouteScreen.class);
                startActivity(newIntent);
                break;
            case R.id.navigation_team:
                if(!walking) {
                    Log.d(TAG, "NOT SAVING TO ROUTE");
                    AccessSharedPrefs.setWalkStartTime(WalkScreen.this, -1);
                }
                newIntent = new Intent(this, TeamScreen.class);
                startActivity(newIntent);
                break;
            default:
                break;
        }
    }
}
