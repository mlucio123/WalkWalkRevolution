package com.example.cse110_project;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.cse110_project.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SBMT12Test {

    @Rule
    public ActivityTestRule<HomeScreen> mActivityTestRule = new ActivityTestRule<>(HomeScreen.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void sBMT12Test() {
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
allOf(withId(R.id.userLastName), withText("x"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
2),
1),
isDisplayed()));
        appCompatEditText4.perform(pressImeActionButton());
        
        ViewInteraction appCompatEditText5 = onView(
allOf(withId(R.id.emailEntry),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
3),
1),
isDisplayed()));
        appCompatEditText5.perform(replaceText("wtf@gmail.com"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText6 = onView(
allOf(withId(R.id.emailEntry), withText("wtf@gmail.com"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
3),
1),
isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());
        
        ViewInteraction appCompatEditText7 = onView(
allOf(withId(R.id.userHeightInch),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
4),
3),
isDisplayed()));
        appCompatEditText7.perform(replaceText("2"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText8 = onView(
allOf(withId(R.id.userHeightFt),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
4),
1),
isDisplayed()));
        appCompatEditText8.perform(replaceText("6"), closeSoftKeyboard());
        
        ViewInteraction appCompatEditText9 = onView(
allOf(withId(R.id.userHeightFt), withText("6"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
4),
1),
isDisplayed()));
        appCompatEditText9.perform(pressImeActionButton());
        
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
        
        ViewInteraction appCompatImageButton = onView(
allOf(withId(R.id.btn_notif),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
0),
0),
isDisplayed()));
        appCompatImageButton.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction button = onView(
allOf(childAtPosition(
childAtPosition(
withId(R.id.notif_container),
0),
3),
isDisplayed()));
        button.check(matches(isDisplayed()));
        
        ViewInteraction button2 = onView(
allOf(withText("Decline"),
childAtPosition(
childAtPosition(
withId(R.id.notif_container),
0),
3)));
        button2.perform(scrollTo(), click());
        
        ViewInteraction button3 = onView(
allOf(childAtPosition(
childAtPosition(
withId(R.id.notif_container),
0),
2),
isDisplayed()));
        button3.check(matches(isDisplayed()));
        
        ViewInteraction button4 = onView(
allOf(withText("Accept"),
childAtPosition(
childAtPosition(
withId(R.id.notif_container),
1),
2)));
        button4.perform(scrollTo(), click());
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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
