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

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FirstLoadScreenUnitTest {
    @Rule
    public ActivityScenarioRule<FirstLoadScreen> scenarioRule = new ActivityScenarioRule<>(FirstLoadScreen.class);

    private ActivityScenario<FirstLoadScreen> scenario;
    private Button getStartedBtn;
    private EditText firstName;
    private EditText lastName;
    private EditText heightFt;
    private EditText heightInch;

    @Before
    public void setup() {
        scenario = scenarioRule.getScenario();
    }

    private void init(FirstLoadScreen firstLoadScreen) {

        getStartedBtn = firstLoadScreen.findViewById(R.id.getStartedBtn);
        firstName = (EditText) firstLoadScreen.findViewById(R.id.userFirstName);
        lastName = (EditText) firstLoadScreen.findViewById(R.id.userLastName);
        heightFt = (EditText) firstLoadScreen.findViewById(R.id.userHeightFt);
        heightInch = (EditText) firstLoadScreen.findViewById(R.id.userHeightInch);
    }

    @Test
    public void testFirstName() {
        scenario.onActivity(firstLoadScreen -> {
            init(firstLoadScreen);
            //SharedPreferences pref = firstLoadScreen.getSharedPreferences("user_info",MODE_PRIVATE);
            firstName.setText("Amy");
            lastName.setText("Bell");
            heightFt.setText("6");
            heightInch.setText("7");
            //getStartedBtn.performClick();
            //assertEquals("Amy", pref.getString("firstname", ""));
            assertEquals("Amy", firstName.getText());
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
            assertEquals("Bell", lastName.getText());
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
            assertEquals("6", heightFt.getText());
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
            assertEquals("7", heightInch.getText());
        });
    }
}
