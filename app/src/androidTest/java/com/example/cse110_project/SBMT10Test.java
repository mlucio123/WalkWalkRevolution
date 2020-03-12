package com.example.cse110_project;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.cse110_project.utils.AccessSharedPrefs;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
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
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
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
public class SBMT10Test {

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
        RouteScreen.isTesting = true;
    }

    @Before
    public void setSharedPrefs() {
        Log.d("SAVING", "PREFS");
        AccessSharedPrefs.setUserInfo(mActivityTestRule.getActivity(), "z",
                "x", 6, 6);
    }

    @After
    public void clearSharedPreferences() {
        mActivityTestRule.getActivity().getSharedPreferences("user_info", MODE_PRIVATE)
                .edit().clear().apply();
    }

    @Test
    public void sBMT10() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.userFirstName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.userFirstName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("z"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.userLastName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("x"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.emailEntry),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("wtf@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.userHeightFt),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("6"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.userHeightInch),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                3),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("2"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.userHeightInch), withText("2"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                3),
                        isDisplayed()));
        appCompatEditText7.perform(pressImeActionButton());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.getStartedBtn), withText("Get Started"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_team),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                3),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.addBtn), withText("+"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                2)));
        appCompatButton2.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.createTeamBtn), withText("CREATE TEAM"),
                        childAtPosition(
                                allOf(withId(R.id.createTeamLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.inviteeName),
                        childAtPosition(
                                allOf(withId(R.id.invitationLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                2)));
        appCompatEditText8.perform(scrollTo(), replaceText("zx"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.inviteeEmail),
                        childAtPosition(
                                allOf(withId(R.id.invitationLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                4)));
        appCompatEditText9.perform(scrollTo(), replaceText("wtf@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.inviteeEmail), withText("wtf@gmail.com"),
                        childAtPosition(
                                allOf(withId(R.id.invitationLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                4)));
        appCompatEditText10.perform(pressImeActionButton());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.inviteeEmail), withText("wtf@gmail.com"),
                        childAtPosition(
                                allOf(withId(R.id.invitationLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                4)));
        appCompatEditText11.perform(pressImeActionButton());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.inviteeEmail), withText("wtf@gmail.com"),
                        childAtPosition(
                                allOf(withId(R.id.invitationLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                4)));
        appCompatEditText12.perform(pressImeActionButton());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.submitBtn), withText("ADD TEAMATE"),
                        childAtPosition(
                                allOf(withId(R.id.invitationLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                5)));
        appCompatButton4.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.navigation_routes),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.walkName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText13.perform(replaceText("g"), closeSoftKeyboard());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.hour),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        appCompatEditText14.perform(replaceText("11"), closeSoftKeyboard());

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.minute),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                3),
                        isDisplayed()));
        appCompatEditText15.perform(replaceText("00"), closeSoftKeyboard());

        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.month),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1),
                        isDisplayed()));
        appCompatEditText16.perform(replaceText("11"), closeSoftKeyboard());

        ViewInteraction appCompatEditText17 = onView(
                allOf(withId(R.id.day),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                3),
                        isDisplayed()));
        appCompatEditText17.perform(replaceText("11"), closeSoftKeyboard());

        ViewInteraction appCompatEditText18 = onView(
                allOf(withId(R.id.year),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                5),
                        isDisplayed()));
        appCompatEditText18.perform(replaceText("2020"), closeSoftKeyboard());

        ViewInteraction appCompatEditText19 = onView(
                allOf(withId(R.id.year), withText("2020"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                5),
                        isDisplayed()));
        appCompatEditText19.perform(pressImeActionButton());

        ViewInteraction button = onView(
                allOf(withId(R.id.submitBtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.submitBtn), withText("Submit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        ViewInteraction button2 = onView(
//                allOf(withId(R.id.scheduleWalkBtn), withText("Schedule Walk"),
//                        childAtPosition(
//                                allOf(withId(R.id.proposedWalkLayout),
//                                        childAtPosition(
//                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
//                                                1)),
//                                9),
//                        isDisplayed()));
//        button2.check(matches(isDisplayed()));
//
//        ViewInteraction button3 = onView(
//                allOf(withId(R.id.withdrawWalkBtn),
//                        childAtPosition(
//                                allOf(withId(R.id.proposedWalkLayout),
//                                        childAtPosition(
//                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
//                                                1)),
//                                10),
//                        isDisplayed()));
//        button3.check(matches(isDisplayed()));

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.scheduleWalkBtn), withText("Schedule Walk"),
                        childAtPosition(
                                allOf(withId(R.id.proposedWalkLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                12)));
        appCompatButton6.perform(scrollTo(), click());
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
