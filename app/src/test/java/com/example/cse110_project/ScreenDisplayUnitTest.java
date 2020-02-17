package com.example.cse110_project;

import android.content.Intent;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.test.core.app.ActivityScenario;
import androidx.test.rule.ActivityTestRule;

import com.example.cse110_project.Firebase.RouteCollection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

public class ScreenDisplayUnitTest {
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(getApplicationContext(), RouteScreen.class);
    }

    @Before
    public void addRoute(){



    }

    @Rule
    public androidx.test.rule.ActivityTestRule<RouteScreen> ActivityTestRule = new ActivityTestRule<>(RouteScreen.class);

    @Test
    public void testNewRouteDisplays() {
        final String name = "examName";
        final String location = "examLocation";
        ActivityScenario<RouteScreen> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(new ActivityScenario.ActivityAction<RouteScreen>() {
            @Override
            public void perform(RouteScreen activity) {

                RouteCollection routeCol = new RouteCollection();
                String deviceID = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                Route testRoute = new Route(name, location);
                routeCol.addRoute(testRoute, deviceID);

                RouteCollection routec = new RouteCollection();
                ArrayList<Route> routeArr = routec.qryRoutes;

                boolean found = false;
                for (int i = 0; i < routeArr.size(); i++ ){
                    if (routeArr.get(i).getName().equals(name)){
                        int ID = valueOf("1404");
                        TextView rName = (TextView) activity.findViewById(ID);
                        if(!found){
                            found = name.equals(rName.getText().toString());
                        }
                    }
                }
                assert(found);
            }
        });
    }
}