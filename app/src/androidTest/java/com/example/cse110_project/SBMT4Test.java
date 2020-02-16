package com.example.cse110_project;


import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SBMT4Test {

    @Rule
    public ActivityTestRule<HomeScreen> mActivityTestRule = new ActivityTestRule<>(HomeScreen.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Before
    public void setSharedPrefs() {
        HomeScreen.USE_GOOGLE_FIT_TESTER = true;
        WalkScreen.USE_TEST_SERVICE = true;
        Log.d("SAVING", "PREFS");
        AccessSharedPrefs.setUserInfo(mActivityTestRule.getActivity(), "Connor",
                "Prendiville", 6, 0);
    }

    @After
    public void clearSharedPreferences() {
        mActivityTestRule.getActivity().getSharedPreferences("user_info", MODE_PRIVATE)
                .edit().clear().apply();
    }

    @Test
    public void sBMT4Test() {
        SystemClock.sleep(2000);
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_routes), withContentDescription("Route"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.addRouteBtn), withText("ADD ROUTE"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.routeName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1)));
        appCompatEditText7.perform(scrollTo(), replaceText("New Route"), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.routeStart),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1)));
        appCompatEditText8.perform(scrollTo(), replaceText("New Routes"), closeSoftKeyboard());
        int routesNum = RouteScreen.getRouteNumber();
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.submitBtn), withText("Submit"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        10),
                                1)));
        appCompatButton3.perform(scrollTo(), click());
        assertEquals(routesNum+1, RouteScreen.getRouteNumber());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
