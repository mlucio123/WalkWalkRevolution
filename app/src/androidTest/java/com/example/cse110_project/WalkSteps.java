package com.example.cse110_project;

import android.content.Intent;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.cse110_project.Firebase.RouteCollection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static java.lang.Integer.valueOf;

@RunWith(AndroidJUnit4.class)
public class WalkSteps {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(getApplicationContext(), WalkScreen.class);
    }

    @Rule
    public androidx.test.rule.ActivityTestRule<WalkScreen> ActivityTestRule = new ActivityTestRule<>(WalkScreen.class);

    @Test
    public void testStepIncr() {
        ActivityScenario<WalkScreen> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(new ActivityScenario.ActivityAction<WalkScreen>() {
            @Override
            public void perform(WalkScreen activity) {

                Button incrDist = activity.findViewById(R.id.startWalkMaterial);
                incrDist.performClick();
                incrDist.performClick();
                TextView stepsDisplay = activity.findViewById(R.id.stepView);
                assert(stepsDisplay.getText().toString().equals("1000"));
            }
        });
    }

    @Test
    public void testDistIncr() {
        ActivityScenario<WalkScreen> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(new ActivityScenario.ActivityAction<WalkScreen>() {
            @Override
            public void perform(WalkScreen activity) {

                Button incrDist = activity.findViewById(R.id.startWalkMaterial);
                incrDist.performClick();
                incrDist.performClick();
                TextView stepsDisplay = activity.findViewById(R.id.distanceView);
                assert(stepsDisplay.getText().toString().equals(".404"));
            }
        });
    }

}
