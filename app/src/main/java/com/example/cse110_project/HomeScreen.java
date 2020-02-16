package com.example.cse110_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import java.math.MathContext;

import com.example.cse110_project.fitness.FitnessService;
import com.example.cse110_project.fitness.FitnessServiceFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class HomeScreen extends AppCompatActivity {

    /* Member Variables */
    private BottomNavigationView bottomNavigationView;
    private Button startWalkBtn;
    private Button addRouteBtn;
    private Button btnUpdateSteps;
    private Button btnBoost;
    private Switch testModeBtn;
    private Chronometer mChronometer;
    private TextView distance;
    private TextView estimatedDistance;
    private TextView textSteps;
//    private com.example.cse110_project.fitness_deprecated.FitnessService fitnessService;
    private FitnessService fitnessService;


    /* Static Variables */
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "HomeScreen";
    private static final int FEET_IN_MILE = 5280;
    private final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 1;
    private String fitnessServiceKey = "GOOGLE_FIT";
    public static Boolean USE_GOOGLE_FIT_TESTER = true;


    /* Member functions */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Assign layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        /**
         * Accessibility Check: shared pref and location access
         */
        // check for shared pref access
        if( AccessSharedPrefs.getFirstName(this).length() == 0 ) {
            launchFirstLoadScreen();
        } else {
            Toast.makeText(HomeScreen.this, "SharedPreference FOUND " +
                    AccessSharedPrefs.getFirstName(this), Toast.LENGTH_SHORT).show();
        }

        // Check for location access
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);

            Toast.makeText(HomeScreen.this, "PERMISSION NONE", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(HomeScreen.this, "LOCATION PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
        }

        if(AccessSharedPrefs.getSavedDistance(this).length() != 0) {
            TextView recentWalkSteps = findViewById(R.id.recentSteps);
            String steps = AccessSharedPrefs.getSavedSteps(this);
            recentWalkSteps.setText(steps);
            TextView recentWalkDist = findViewById(R.id.recentDist);
            recentWalkDist.setText(AccessSharedPrefs.getSavedDistance(this));
            TextView recentTimeView = findViewById(R.id.recentTime);
            recentTimeView.setText(AccessSharedPrefs.getSavedTimer(this));
            LinearLayout recentWalkStats = findViewById(R.id.recentWalkLayout);
            recentWalkStats.setVisibility(View.VISIBLE);
        }

        /*if(AccessSharedPrefs.getWalkStartTime(this) != -1 && !WalkScreen.walking) {
            Log.d(TAG, "Overwriting time");
            AccessSharedPrefs.setWalkStartTime(this, -1);
        }*/

        /* TEST MODE BUTTON */
        testModeBtn = findViewById(R.id.testMode);
        if(USE_GOOGLE_FIT_TESTER) {
            testModeBtn.setText("TEST");
        } else {
            testModeBtn.setText("NORMAL");
        }

        testModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                USE_GOOGLE_FIT_TESTER = !USE_GOOGLE_FIT_TESTER;

                if (USE_GOOGLE_FIT_TESTER) {
                    testModeBtn.setText("TEST");
                    Toast.makeText(HomeScreen.this, "TEST MODE: ON", Toast.LENGTH_SHORT).show();
                } else {
                    testModeBtn.setText("NORMAL");
                    Toast.makeText(HomeScreen.this, "TEST MODE: OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /**
         * Create and start fitnessService
         */
        fitnessService = FitnessServiceFactory.create(this, USE_GOOGLE_FIT_TESTER);
        fitnessService.setup();

        // initialize text views
        textSteps = findViewById(R.id.homeDailyStepsCount);
        distance = findViewById(R.id.homeDailyDistanceCount);
        estimatedDistance = findViewById(R.id.homeDailyEstimateCount);

        // update button for step count
        btnUpdateSteps = findViewById(R.id.buttonUpdateSteps);
        btnUpdateSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Update steps and distance
                long dailySteps = fitnessService.getDailySteps();
                long dailyDistance = fitnessService.getDailyDistance();
                textSteps.setText(String.valueOf(dailySteps) + " Steps");
                distance.setText(String.valueOf(dailyDistance) + " Miles");
                estimatedDistance.setText(String.valueOf(dailyDistance) + " Miles");
            }
        });

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
                Intent intent = new Intent(HomeScreen.this, RouteFormScreen.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//       If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.listActiveSubscriptions();
//                 TODO: updateStep ..
//                fitnessService.updateStepCount();
//                fitnessService.readHistoryData();
            }
            Log.d(TAG, "RESULT_OK");

        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
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
                newIntent.putExtra("actFlag", "Home");
                newIntent.putExtra("is_test", USE_GOOGLE_FIT_TESTER);
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

    public void setDistance(long distanceValue){
        distance.setText(String.valueOf(distanceValue) + " Miles");
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
            //String estDistStr = rounded + "@string/space" + "@string/feetStr";
            estimatedDistance.setText(rounded + " Feet");
            distance.setText(rounded + " Feet");
        } else {
            double convert = (estimateDistance * 1.0 / FEET_IN_MILE );
            bd = new BigDecimal(convert);
            bd = bd.round(new MathContext(3));
            rounded = bd.doubleValue();
            String estDistStr = rounded + "@string/space" + "@string/milesStr";
            estimatedDistance.setText(rounded + " Miles");
            distance.setText(rounded + " Miles");
        }

    }

    public void launchFirstLoadScreen() {
        Intent intent = new Intent(this, FirstLoadScreen.class);
        startActivity(intent);
    }

}
