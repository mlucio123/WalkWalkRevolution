package com.example.cse110_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class FirstLoadScreenTest {
    @Rule
    public ActivityTestRule<FirstLoadScreen> firstLoadScreenTest = new ActivityTestRule<>(FirstLoadScreen.class);

    private Button getStartedBtn;
    private EditText firstName;
    private EditText lastName;
    private EditText heightFt;
    private EditText heightInch;

/*    @Test
    public void testSharedPref() {
        FirstLoadScreen firstLoadScreen = firstLoadScreenTest.getActivity();
        SharedPreferences pref = firstLoadScreen.getSharedPreferences("user_info",MODE_PRIVATE);
        getStartedBtn = firstLoadScreen.findViewById(R.id.getStartedBtn);
        firstName = (EditText) firstLoadScreen.findViewById(R.id.userFirstName);
        lastName = (EditText) firstLoadScreen.findViewById(R.id.userLastName);
        heightFt = (EditText) firstLoadScreen.findViewById(R.id.userHeightFt);
        heightInch = (EditText) firstLoadScreen.findViewById(R.id.userHeightInch);
        firstName.setText("Amy");
        lastName.setText("Bell");
        heightFt.setText("6");
        heightInch.setText("7");
        getStartedBtn.performClick();
        assertEquals("Amy", pref.getString("firstname", ""));

    }*/

    @Test
    public void testFirstNameDisplay() {
        FirstLoadScreen firstLoadScreen = firstLoadScreenTest.getActivity();
        firstName = (EditText) firstLoadScreen.findViewById(R.id.userFirstName);
        assertEquals("First Name", firstName.getHint());
    }

    @Test
    public void testLastNameDisplay() {
        FirstLoadScreen firstLoadScreen = firstLoadScreenTest.getActivity();
        lastName = (EditText) firstLoadScreen.findViewById(R.id.userLastName);
        assertEquals("Last Name", lastName.getHint());
    }

    @Test
    public void testFtDisplay() {
        FirstLoadScreen firstLoadScreen = firstLoadScreenTest.getActivity();
        heightFt = (EditText) firstLoadScreen.findViewById(R.id.userHeightFt);
        assertEquals("0", heightFt.getHint());
    }

    @Test
    public void testInchDisplay() {
        FirstLoadScreen firstLoadScreen = firstLoadScreenTest.getActivity();
        heightInch = (EditText) firstLoadScreen.findViewById(R.id.userHeightInch);
        assertEquals("0", heightInch.getHint());
    }

}