package com.example.cse110_project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cse110_project.utils.Route;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class GoogleMapNavigationTest {

    final String URL = "https://www.google.com/maps/search/?api=1&query=";

    GoogleMapNavigation googleMapNavigation;

    String ucsd = "UCSD";
    String ucsdFullName = "University of California, San Diego";
    String dummyName = "dummyName";
    Route route;


    @Before
    public void setup() {
        googleMapNavigation  = new GoogleMapNavigation();
    }

    @Test
    public void urlTest() {
        assertEquals(googleMapNavigation.getUrlPrefix(), URL);
    }

    @Test
    public void startPositionTest() {
        googleMapNavigation = new GoogleMapNavigation(ucsd);
        assertEquals(ucsd, googleMapNavigation.getStartPosition());
    }

    @Test
    public void getQueryDirectTest() {
        googleMapNavigation = new GoogleMapNavigation(ucsd);
        googleMapNavigation.getURL();
        assertEquals(URL+ucsd, googleMapNavigation.getQuery());
    }

    @Test
    public void getQueryDirectWithSapceTest() {
        googleMapNavigation = new GoogleMapNavigation(ucsdFullName);
        googleMapNavigation.getURL();
        assertEquals(URL+ucsdFullName.replace(" ", "+"), googleMapNavigation.getQuery());
    }

    @Test
    public void locationWithRegInput() {
        String actual = new Route(dummyName, ucsd).getStartingPoint();
        googleMapNavigation  = new GoogleMapNavigation(actual);
        googleMapNavigation.getURL();
        actual = googleMapNavigation.getQuery();
        assertEquals(URL + ucsd, actual);
    }

    @Test
    public void locationWithSpace() {
        String actual = new Route(dummyName, ucsdFullName).getStartingPoint();
        googleMapNavigation  = new GoogleMapNavigation(actual);
        googleMapNavigation.getURL();
        actual = googleMapNavigation.getQuery();
        System.out.println(actual);
        assertEquals(URL + ucsdFullName.replace(" ", "+"), actual);
    }
}
