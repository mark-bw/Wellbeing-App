package com.sociallaboursupply.sls_wellbeing_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.KeyEvent;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class MentalActivityTest {

    // This rule starts the specified activity
    @Rule
    public ActivityScenarioRule<NeedHelpActivity> activityRule= new ActivityScenarioRule<>(NeedHelpActivity.class);

    // Find learning button to test that the need help page has launched
    @Test
    public void testLaunch(){
        onView(withId(R.id.buttonHelpPageLearning)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToWorkActivity(){
        onView(withId(R.id.buttonHelpPageWork)).perform(scrollTo(), click());

        // Test work activity launches and back again
        onView(withId(R.id.textViewCatchUpDate)).check(matches(isDisplayed())).perform(pressKey(KeyEvent.KEYCODE_BACK));
        testLaunch();
    }
}