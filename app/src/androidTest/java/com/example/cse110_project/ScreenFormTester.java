package com.example.cse110_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.robolectric.Robolectric;
//import org.robolectric.shadows.ShadowToast;


import static androidx.test.core.app.ApplicationProvider.getApplicationContext;


@RunWith(AndroidJUnit4.class)
public class ScreenFormTester {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(getApplicationContext(), RouteFormScreen.class);
    }

    @Rule
    public androidx.test.rule.ActivityTestRule<RouteFormScreen> ActivityTestRule = new ActivityTestRule<>(RouteFormScreen.class);

    @Test
    public void testFormPopulatesRoute() {
        final String name = "exampleName";
        final String location = "exampleLocation";
        ActivityScenario<RouteFormScreen> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(new ActivityScenario.ActivityAction<RouteFormScreen>() {
            @Override
            public void perform(RouteFormScreen activity) {
                EditText nameText = activity.findViewById(R.id.routeName);
                nameText.setText(name);

                EditText locationText = activity.findViewById(R.id.routeStart);
                locationText.setText(location);

                activity.newRoute.getName();

            }
        });



    }
}
