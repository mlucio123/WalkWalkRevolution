package com.example.cse110_project;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.cse110_project.fitness_deprecated.FitnessService;
import com.example.cse110_project.fitness_deprecated.FitnessServiceFactory;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
public class SetSharedPrefsTest {

    private static final String TEST_SERVICE = "TEST_SERVICE";

    @Rule
    public ActivityTestRule<FirstLoadScreen> mActivityTestRule = new ActivityTestRule<>(FirstLoadScreen.class);

    @Before
    @After
    public void clearSharedPrefs() {
        SharedPreferences.Editor editor = mActivityTestRule.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void setSharedPrefsTest() {

        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(HomeScreen homeScreen) {
                return new TestFitnessService(homeScreen);
            }
        });

        mActivityTestRule.getActivity().setFitnessServiceKey(TEST_SERVICE);

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
        appCompatEditText3.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.userHeightInch),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.getStartedBtn), withText("Get Started"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());

        SharedPreferences prefs = mActivityTestRule.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);

        String first = prefs.getString("firstname", "");
        String last = prefs.getString("lastname", "");
        int height = prefs.getInt("heightFt", -1);
        int inch = prefs.getInt("heightInch", -1);

        Assert.assertEquals("Connor", first);
        Assert.assertEquals("P", last);
        Assert.assertEquals(5, height);
        Assert.assertEquals(5, inch);



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

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private HomeScreen homeScreen;

        public TestFitnessService(HomeScreen stepCountActivity) {
            this.homeScreen = stepCountActivity;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void readHistoryData() {

        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            homeScreen.setStepCount(323);
        }
    }

}
