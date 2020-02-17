package com.example.cse110_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.cse110_project.Firebase.RouteCollection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.robolectric.Robolectric;
//import org.robolectric.shadows.ShadowToast;


import java.util.ArrayList;
import java.util.Arrays;

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

                Button submit = activity.findViewById(R.id.submitBtn);
                submit.performClick();

                RouteCollection routec = new RouteCollection();
                ArrayList<Route> routeArr = routec.qryRoutes;

                boolean found = false;
                for (int i = 0; i < routeArr.size(); i++ ){
                    if (routeArr.get(i).getName().equals(name)){
                        found = true;
                    }
                }
                assert(found);
            }
        });
    }

    @Test
    public void testFormPopulatesRouteTags() {
        //boolean[] tags ={out, loop, flat, hills, even, rough, street, trail, easy, medium, hard, favorite};
        boolean[] testTags = {true, false, true, false, false, true, true, false, false, true, false, false};
        final String name = "testRouteTags";
        final String location = "exStreet";
        ActivityScenario<RouteFormScreen> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(new ActivityScenario.ActivityAction<RouteFormScreen>() {
            @Override
            public void perform(RouteFormScreen activity) {
                EditText nameText = activity.findViewById(R.id.routeName);
                nameText.setText(name);

                EditText locationText = activity.findViewById(R.id.routeStart);
                locationText.setText(location);

                Button outTag = activity.findViewById(R.id.routeStyleOutAndBack);
                outTag.performClick();

                Button flatTag = activity.findViewById(R.id.routeLandFlat);
                flatTag.performClick();

                Button roughTag = activity.findViewById(R.id.surfaceRough);
                roughTag.performClick();

                Button streetTag = activity.findViewById(R.id.routeTypeStreets);
                streetTag.performClick();

                Button mediumTag = activity.findViewById(R.id.moderateBtn);
                mediumTag.performClick();

                Button submit = activity.findViewById(R.id.submitBtn);
                submit.performClick();

                RouteCollection routec = new RouteCollection();
                ArrayList<Route> routeArr = routec.qryRoutes;

                boolean found = false;


                for (int i = 0; i < routeArr.size(); i++ ){
                    if (Arrays.equals(routeArr.get(i).getTags(), testTags)){
                        found = true;
                    }
                }
                assert(found);
            }
        });
    }


}
