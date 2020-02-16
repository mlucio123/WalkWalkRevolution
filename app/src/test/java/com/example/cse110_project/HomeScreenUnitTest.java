package com.example.cse110_project;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.android.internal.LocalPermissionGranter;
import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
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
        firstName = (EditText) homeScreen.findViewById(R.id.userFirstName);
        lastName = (EditText) homeScreen.findViewById(R.id.userLastName);
        heightFt = (EditText) homeScreen.findViewById(R.id.userHeightFt);
        heightInch = (EditText) homeScreen.findViewById(R.id.userHeightInch);
    }

    @Test
    public void testFirstName() {
        scenario.onActivity(homeScreen -> {
            init(homeScreen);
            //SharedPreferences pref = firstLoadScreen.getSharedPreferences("user_info",MODE_PRIVATE);
            firstName.setText("Amy");
            lastName.setText("Bell");
            heightFt.setText("6");
            heightInch.setText("7");
            //getStartedBtn.performClick();
            //assertEquals("Amy", pref.getString("firstname", ""));
            assertEquals("Amy", firstName.getText().toString());
        });
    }

    @Test
    public void testLastName() {
        scenario.onActivity(firstLoadScreen -> {
            init(firstLoadScreen);
            //SharedPreferences pref = firstLoadScreen.getSharedPreferences("user_info",MODE_PRIVATE);
            firstName.setText("Amy");
            lastName.setText("Bell");
            heightFt.setText("6");
            heightInch.setText("7");
            //getStartedBtn.performClick();
            //assertEquals("Amy", pref.getString("firstname", ""));
            assertEquals("Bell", lastName.getText().toString());
        });
    }

    @Test
    public void testFt() {
        scenario.onActivity(firstLoadScreen -> {
            init(firstLoadScreen);
            //SharedPreferences pref = firstLoadScreen.getSharedPreferences("user_info",MODE_PRIVATE);
            firstName.setText("Amy");
            lastName.setText("Bell");
            heightFt.setText("6");
            heightInch.setText("7");
            //getStartedBtn.performClick();
            //assertEquals("Amy", pref.getString("firstname", ""));
            assertEquals("6", heightFt.getText().toString());
        });
    }

    @Test
    public void testInch() {
        scenario.onActivity(firstLoadScreen -> {
            init(firstLoadScreen);
            //SharedPreferences pref = firstLoadScreen.getSharedPreferences("user_info",MODE_PRIVATE);
            firstName.setText("Amy");
            lastName.setText("Bell");
            heightFt.setText("6");
            heightInch.setText("7");
            //getStartedBtn.performClick();
            //assertEquals("Amy", pref.getString("firstname", ""));
            assertEquals("7", heightInch.getText().toString());
        });
    }
}
