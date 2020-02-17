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
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SBMT2Test {

    @Rule
    public ActivityTestRule<HomeScreen> mActivityTestRule = new ActivityTestRule<>(HomeScreen.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @BeforeClass
    public static void setUp() {
        HomeScreen.USE_GOOGLE_FIT_TESTER = true;
        WalkScreen.USE_TEST_SERVICE = true;
    }

    @Before
    public void setSharedPrefs() {
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
    public void sBMT2() {
        SystemClock.sleep(5000);
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.userFirstName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Connor"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.userLastName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("P"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.userHeightFt),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("6"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.userHeightInch),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("0"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.getStartedBtn), withText("Get Started"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());
        SystemClock.sleep(2000);
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.startBtn), withText("START WALK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatButton2.perform(click());

        SystemClock.sleep(500);

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.startWalkMaterial), withText("Start Walking!"),
                        childAtPosition(
                                allOf(withId(R.id.startStopContainer),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                6)),
                                1),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.stopWalkMaterial), withText("End My Walk"),
                        childAtPosition(
                                allOf(withId(R.id.startStopContainer),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                6)),
                                0),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.routeName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1)));
        appCompatEditText5.perform(scrollTo(), replaceText("Regular Walk"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.routeStart),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1)));
        appCompatEditText6.perform(scrollTo(), replaceText("Stresseman and Bragg"), closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.submitBtn), withText("Submit"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        10),
                                1)));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.routeTitle), withText("Route"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Route")));
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
