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

public class MainActivityTest {

    // This rule starts the specified activity
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule= new ActivityScenarioRule<>(MainActivity.class);

    // Find home mood tracker button to test that the login page has launched
    @Test
    public void testLaunch(){
        onView(withId(R.id.button_moodTracker)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToMoodTrackerActivity(){
        onView(withId(R.id.button_moodTracker)).perform(scrollTo(), click());

        // Test mood track activity launches and back again
        onView(withId(R.id.chartMoodTracker)).check(matches(isDisplayed())).perform(pressKey(KeyEvent.KEYCODE_BACK));
        testLaunch();
    }

    @Test
    public void testNavigateToTasksActivity(){
        onView(withId(R.id.button_tasks)).perform(scrollTo(), click());

        // Test tasks activity launches
        onView(withId(R.id.recyclerViewTasksList)).check(matches(isDisplayed())).perform(pressKey(KeyEvent.KEYCODE_BACK));
        testLaunch();
    }

    @Test
    public void testNavigateToNeedHelpActivity(){
        onView(withId(R.id.button_help)).perform(scrollTo(), click());

        // Test help activity launches
        onView(withId(R.id.buttonHelpPageLearning)).check(matches(isDisplayed())).perform(pressKey(KeyEvent.KEYCODE_BACK));
        testLaunch();
    }
}