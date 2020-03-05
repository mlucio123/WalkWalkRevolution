package com.example.cse110_project;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class HomeScreenUnitTest {
    @Rule
    public ActivityScenarioRule<HomeScreen> scenarioRule = new ActivityScenarioRule<>(HomeScreen.class);

    private ActivityScenario<HomeScreen> scenario;
    private Button getStartedBtn;
    private EditText firstName;
    private EditText lastName;
    private EditText heightFt;
    private EditText heightInch;


    private TextView pageTitle;
    private TextView stepCount;
    private TextView distanceCount;
    private TextView estimatedDistanceCount;

    @Before
    public void setup() {
        scenario = scenarioRule.getScenario();
    }

    private void init(HomeScreen homeScreen) {

        getStartedBtn = homeScreen.findViewById(R.id.getStartedBtn);

        pageTitle = homeScreen.findViewById(R.id.homeTitle);
        stepCount = homeScreen.findViewById(R.id.homeDailyStepsCount);
        distanceCount = homeScreen.findViewById(R.id.homeDailyDistanceCount);
        estimatedDistanceCount = homeScreen.findViewById(R.id.homeDailyEstimateCount);

    }


    @Test
    public void testComponentExists() {
        scenario.onActivity(homeScreen -> {

            init(homeScreen);
            assertEquals(pageTitle.getText().toString(), "Home");
            assertNotNull(stepCount.getText().toString());
            assertNotNull(distanceCount.getText().toString());
            assertNotNull(estimatedDistanceCount.getText().toString());

        });
    }



}
