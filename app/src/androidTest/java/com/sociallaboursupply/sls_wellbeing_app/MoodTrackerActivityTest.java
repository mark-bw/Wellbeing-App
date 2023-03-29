package com.sociallaboursupply.sls_wellbeing_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class MoodTrackerActivityTest {

    // This rule starts the specified activity
    @Rule
    public ActivityScenarioRule<MoodTrackerActivity> activityRule= new ActivityScenarioRule<>(MoodTrackerActivity.class);

    // Find mood chart to test that the mood tracker page has launched
    @Test
    public void testLaunch(){
        onView(withId(R.id.chartMoodTracker)).check(matches(isDisplayed()));
    }
}