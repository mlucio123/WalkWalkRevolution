package com.example.cse110_project;

import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)

public class WalkScreenTest {
    @Rule
    public ActivityScenarioRule<WalkScreen> scenarioRule = new ActivityScenarioRule<>(WalkScreen.class);

    private ActivityScenario<WalkScreen> scenario;

    private Button startWalkMaterial;

    private TextView route_summary_title;
    private TextView walkPageTitle;

    @Before
    public void setup() {
        scenario = scenarioRule.getScenario();
    }

    private void init(WalkScreen walkScreen) {
        startWalkMaterial = walkScreen.findViewById(R.id.startWalkMaterial);
        route_summary_title = walkScreen.findViewById(R.id.route_summary_title);
        walkPageTitle = walkScreen.findViewById(R.id.walkPageTitle);
    }

    @Test
    public void testFirstName() {
        scenario.onActivity(walkScreen -> {
            init(walkScreen);
            assertEquals("Start Walking!", startWalkMaterial.getText().toString());
            assertEquals("This is a new route!", route_summary_title.getText().toString());
            assertEquals("Walk", walkPageTitle.getText().toString());
        });
    }

}
