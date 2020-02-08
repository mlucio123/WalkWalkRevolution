package com.example.cse110_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;

import com.example.cse110_project.fitness.FitnessServiceFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.cse110_project.StrideCalculator;

public class HomeScreen extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Button startWalkBtn;
    private Button addRouteBtn;
    private Chronometer mChronometer;
    private Button btnUpdateSteps;
    private Button btnBoost;

    private TextView distance;
    private TextView estimatedDistance;


    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "HomeScreen";
    private static final int FEET_IN_MILE = 5280;
    private TextView textSteps;
    private com.example.cse110_project.fitness.FitnessService fitnessService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        // initialize text views
        textSteps = findViewById(R.id.homeDailyStepsCount);
        distance = findViewById(R.id.homeDailyDistanceCount);
        estimatedDistance = findViewById(R.id.homeDailyEstimateCount);


        // google fit initialize
        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);


        // update button for step count
        btnUpdateSteps = findViewById(R.id.buttonUpdateSteps);
        btnUpdateSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitnessService.updateStepCount();

            }
        });

       fitnessService.setup();

        // bottom navigation bar implementation
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return false;
            }
        });


        // TODO: STEP BOOST

        btnBoost = findViewById(R.id.boostBtn);
        btnBoost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String steps = textSteps.getText().toString();

                int curr = Integer.parseInt(steps.substring(0,steps.indexOf(" ")));
                setStepCount(curr + 500);
            }
        });


        // timer set up
        mChronometer = findViewById(R.id.homeTimer);
        mChronometer.setVisibility(View.INVISIBLE);


        // start walking button implementation, intentional walk
        startWalkBtn = (Button) findViewById(R.id.startBtn);
        startWalkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWalk();
                System.out.println("BEFORE CHECK");
                if(getIntent().hasExtra("START_TIME")) {
                    long base = getIntent().getLongExtra("START_TIME", 0L);
                    startTimer(base);

                }
                System.out.println("AFTER CHECK");
            }
        });


        // add route button
        addRouteBtn = (Button) findViewById(R.id.addBtn);
        addRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, RouteScreen.class);
                startActivity(intent);
            }
        });

    }


    // method used to navigate across navigation bar

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


    // start intent to walk screen
    public void launchWalk() {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//       If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
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

        if (estimateDistance < FEET_IN_MILE){
            estimatedDistance.setText(rounded + " Feet");
        } else {
            double convert = (estimateDistance * 1.0 / FEET_IN_MILE );
            bd = new BigDecimal(convert);
            bd = bd.round(new MathContext(3));
            rounded = bd.doubleValue();
            estimatedDistance.setText(rounded + " Miles");
        }

    }
}
