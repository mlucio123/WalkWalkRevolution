package com.example.cse110_project;

import android.provider.Settings;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.cse110_project.utils.AccessSharedPrefs;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ActivityScenario.launch;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NotificationScreenTest {

    @Rule
    public ActivityScenarioRule<NotificationScreen> scenarioRule = new ActivityScenarioRule<>(NotificationScreen.class);

    private ActivityScenario<NotificationScreen> scenario;

    private TextView textView;
    private CollectionReference firebase;

    @Before
    public void setup() {
        scenario = scenarioRule.getScenario();
        firebase = FirebaseFirestore.getInstance().collection("users");

    }

    private void init(NotificationScreen notificationScreen) {
        textView = notificationScreen.findViewById(R.id.textView);
    }


    @Test
    public void testEmptyScreen() {
        scenario.onActivity(activity -> {
            init(activity);
            assertEquals("Notifications", textView.getText().toString());
        });

    }

}
