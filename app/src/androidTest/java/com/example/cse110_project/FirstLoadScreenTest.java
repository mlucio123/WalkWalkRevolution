package com.example.cse110_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
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

    @Before
    @After
    public void clearSharedPreferences() {
        firstLoadScreenTest.getActivity().getSharedPreferences("user_info", MODE_PRIVATE)
                .edit().clear().apply();
    }

    @Test
    public void testFirstNameDisplay() {
        FirstLoadScreen firstLoadScreen = firstLoadScreenTest.getActivity();
        firstName = (EditText) firstLoadScreen.findViewById(R.id.userFirstName);
        assertEquals("First Name", firstName.getHint().toString());
    }

    @Test
    public void testLastNameDisplay() {
        FirstLoadScreen firstLoadScreen = firstLoadScreenTest.getActivity();
        lastName = (EditText) firstLoadScreen.findViewById(R.id.userLastName);
        assertEquals("Last Name", lastName.getHint().toString());
    }

    @Test
    public void testFtDisplay() {
        FirstLoadScreen firstLoadScreen = firstLoadScreenTest.getActivity();
        heightFt = (EditText) firstLoadScreen.findViewById(R.id.userHeightFt);
        assertEquals("0", heightFt.getHint().toString());
    }

    @Test
    public void testInchDisplay() {
        FirstLoadScreen firstLoadScreen = firstLoadScreenTest.getActivity();
        heightInch = (EditText) firstLoadScreen.findViewById(R.id.userHeightInch);
        assertEquals("0", heightInch.getHint().toString());
    }

}

