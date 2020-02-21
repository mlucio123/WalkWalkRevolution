package com.example.cse110_project;

import androidx.test.filters.LargeTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@LargeTest
public class RouteTest {
    private Route testRoute = new Route("testRoute", "testStart");

    @Test
    public void redundantCheck() {
        assertEquals(testRoute.getName(), "testRoute");
        assertEquals(testRoute.getStartingPoint(), "testStart");
    }

    @Test
    public void setGetFav() {
        assertFalse(testRoute.getFavorite());
        testRoute.setFavorite(true);
        assertTrue(testRoute.getFavorite());
    }

    @Test
    public void emptyNotes() {
        assertNull(testRoute.getNotes());
    }

    @Test
    public void setNotes() {
        testRoute.setNotes("Fun Walk");
        assertEquals("Fun Walk", testRoute.getNotes());
    }

}