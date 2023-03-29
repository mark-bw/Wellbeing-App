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

public class LearningActivityTest {

    // This rule starts the specified activity
    @Rule
    public ActivityScenarioRule<LearningActivity> activityRule= new ActivityScenarioRule<>(LearningActivity.class);

    // Find mood chart to test that the mood tracker page has launched
    @Test
    public void testLaunch(){
        onView(withId(R.id.buttonLearningPageCourses)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToCoursesActivity() {
        onView(withId(R.id.buttonLearningPageCourses)).perform(scrollTo(), click());

        // Test learning activity launches and back again
        onView(withId(R.id.youtubePlayerViewVideoOfTheDay)).check(matches(isDisplayed())).perform(pressKey(KeyEvent.KEYCODE_BACK));
        testLaunch();
    }

    @Test
    public void testNavigateToLearningResourcesActivity() {
        onView(withId(R.id.buttonLearningPageResources)).perform(scrollTo(), click());

        // Test learning activity launches and back again
        onView(withId(R.id.recyclerViewResources)).check(matches(isDisplayed())).perform(pressKey(KeyEvent.KEYCODE_BACK));
        testLaunch();
    }
}