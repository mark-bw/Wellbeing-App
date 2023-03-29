package com.sociallaboursupply.sls_wellbeing_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class MentorActivityTest {

    // This rule starts the specified activity
    @Rule
    public ActivityScenarioRule<MentorActivity> activityRule= new ActivityScenarioRule<>(MentorActivity.class);

    // Find mood chart to test that the mood tracker page has launched
    @Test
    public void testLaunch(){
        onView(withId(R.id.textViewCatchUpDate)).check(matches(isDisplayed()));
    }
}