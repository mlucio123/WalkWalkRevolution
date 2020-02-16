package com.example.cse110_project;


import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
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
    @Before
    public void setSharedPrefs() {
        Log.d("SAVING", "PREFS");
        AccessSharedPrefs.setUserInfo(mActivityTestRule.getActivity(), "Connor",
                "Prendiville", 6, 0);
    }

    /*@After
    public void clearSharedPrefs()
    }*/

    @Test
    public void sBMT2Test() {
        Log.d("IN", "TEST");
        ViewInteraction textView = onView(
                allOf(withId(R.id.homeTitle), withText("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Home")));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.startBtn), withText("START WALK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatButton2.perform(click());

        SystemClock.sleep(5000);

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
                allOf(withId(R.id.boostStepBtn), withText("ADD STEP"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.startStopContainer),
                                        3),
                                0),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.boostStepBtn), withText("ADD STEP"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.startStopContainer),
                                        3),
                                0),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.boostStepBtn), withText("ADD STEP"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.startStopContainer),
                                        3),
                                0),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.boostStepBtn), withText("ADD STEP"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.startStopContainer),
                                        3),
                                0),
                        isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.boostStepBtn), withText("ADD STEP"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.startStopContainer),
                                        3),
                                0),
                        isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.stopWalkMaterial), withText("End My Walk"),
                        childAtPosition(
                                allOf(withId(R.id.startStopContainer),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                6)),
                                0),
                        isDisplayed()));
        appCompatButton9.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.newRouteTitle), withText("New Route"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("New Route")));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.routeName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1)));
        appCompatEditText5.perform(scrollTo(), click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.routeName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1)));
        appCompatEditText6.perform(scrollTo(), click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.routeName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1)));
        appCompatEditText7.perform(scrollTo(), click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.routeName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1)));
        appCompatEditText8.perform(scrollTo(), replaceText("Regular Walk"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.routeStart),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1)));
        appCompatEditText9.perform(scrollTo(), replaceText("Stressman and Bragg"), closeSoftKeyboard());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(R.id.submitBtn), withText("Submit"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        10),
                                1)));
        appCompatButton10.perform(scrollTo(), click());

        SystemClock.sleep(10000);

        ViewInteraction button = onView(
                allOf(withText("Expand"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withText("1.17 Miles"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(LinearLayout.class),
                                        2),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("1.17 Miles")));

        ViewInteraction textView4 = onView(
                allOf(withText("2500 Steps"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(LinearLayout.class),
                                        3),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("2500 Steps")));

        ViewInteraction textView5 = onView(
                allOf(withText("Regular Walk"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView5.check(matches(withText("Regular Walk")));

        ViewInteraction textView6 = onView(
                allOf(withText("Stressman and Bragg"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(LinearLayout.class),
                                        1),
                                1),
                        isDisplayed()));
        textView6.check(matches(withText("Stressman and Bragg")));
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

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
}
