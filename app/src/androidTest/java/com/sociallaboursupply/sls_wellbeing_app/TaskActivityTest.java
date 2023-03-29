package com.sociallaboursupply.sls_wellbeing_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class TaskActivityTest {

    // This rule starts the specified activity
    @Rule
    public ActivityScenarioRule<TaskActivity> activityRule= new ActivityScenarioRule<>(TaskActivity.class);

    // Find recycler view to test that the tasks page has launched
    @Test
    public void testLaunch(){
        onView(withId(R.id.recyclerViewTasksList)).check(matches(isDisplayed()));
    }
}