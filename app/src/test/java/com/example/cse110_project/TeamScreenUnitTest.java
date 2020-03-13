package com.example.cse110_project;

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
public class TeamScreenUnitTest {
    @Rule
    public ActivityScenarioRule<TeamScreen> scenarioRule = new ActivityScenarioRule<>(TeamScreen.class);

    private ActivityScenario<TeamScreen> scenario;
    private TextView title;

    @Before
    public void setup() {
        scenario = scenarioRule.getScenario();
    }

    private void init(TeamScreen teamScreen) {

        title = teamScreen.findViewById(R.id.teamTitle);

    }

    @Test
    public void test() {
        scenario.onActivity(teamScreen -> {
            init(teamScreen);
            title = teamScreen.findViewById(R.id.teamTitle);
            assertEquals("Team", title.getText().toString());
        });
    }
}
