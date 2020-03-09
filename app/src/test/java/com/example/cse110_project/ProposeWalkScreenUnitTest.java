package com.example.cse110_project;

import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ProposeWalkScreenUnitTest {
    @Rule
    public ActivityScenarioRule<ProposeWalkScreen> scenarioRule = new ActivityScenarioRule<>(ProposeWalkScreen.class);

    private ActivityScenario<ProposeWalkScreen> scenario;
    private Button submitBtn;
    private EditText routeName;
    //private EditText startPosition;
    private EditText hour;
    private EditText minute;
    private EditText month;
    private EditText day;

    @Before
    public void setup() {
        scenario = scenarioRule.getScenario();
    }

    private void init(ProposeWalkScreen proposeWalkScreen) {

        submitBtn = proposeWalkScreen.findViewById(R.id.submitBtn);
        routeName = (EditText) proposeWalkScreen.findViewById(R.id.routeName);
        //startPosition = (EditText) proposeWalkScreen.findViewById(R.id.startPosition);
        hour = (EditText) proposeWalkScreen.findViewById(R.id.hour);
        minute = (EditText) proposeWalkScreen.findViewById(R.id.minute);
        month = (EditText) proposeWalkScreen.findViewById(R.id.month);
        day = (EditText) proposeWalkScreen.findViewById(R.id.day);
    }

    @Test
    public void testRouteName() {
        scenario.onActivity(proposeWalkScreen -> {
            init(proposeWalkScreen);
            routeName.setText("Daily Walk");
            assertEquals("Daily Walk", routeName.getText().toString());
        });
    }

    /*@Test
    public void testStartPosition() {
        scenario.onActivity(proposeWalkScreen -> {
            init(proposeWalkScreen);
            startPosition.setText("Home");
            assertEquals("Home", startPosition.getText().toString());
        });
    }*/

    @Test
    public void testHour() {
        scenario.onActivity(proposeWalkScreen -> {
            init(proposeWalkScreen);
            hour.setText("08");
            assertEquals("08", hour.getText().toString());
        });
    }

    @Test
    public void testMin() {
        scenario.onActivity(proposeWalkScreen -> {
            init(proposeWalkScreen);
            minute.setText("30");
            assertEquals("30", minute.getText().toString());
        });
    }

    @Test
    public void testMonth() {
        scenario.onActivity(proposeWalkScreen -> {
            init(proposeWalkScreen);
            month.setText("03");
            assertEquals("03", month.getText().toString());
        });
    }

    @Test
    public void testDay() {
        scenario.onActivity(proposeWalkScreen -> {
            init(proposeWalkScreen);
            day.setText("15");
            assertEquals("15", day.getText().toString());
        });
    }
}
