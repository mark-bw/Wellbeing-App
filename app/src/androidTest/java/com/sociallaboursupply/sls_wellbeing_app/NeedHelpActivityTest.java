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

public class NeedHelpActivityTest {

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

    @Test
    public void testNavigateToLearningActivity(){
        onView(withId(R.id.buttonHelpPageLearning)).perform(scrollTo(), click());

        // Test learning activity launches and back again
        onView(withId(R.id.buttonLearningPageCourses)).check(matches(isDisplayed())).perform(pressKey(KeyEvent.KEYCODE_BACK));
        testLaunch();
    }

    @Test
    public void testNavigateToMentalActivity(){
        onView(withId(R.id.buttonHelpPageMental)).perform(scrollTo(), click());

        // Test mental activity launches and back again
        onView(withId(R.id.buttonMentalPageNeedMentor)).check(matches(isDisplayed())).perform(pressKey(KeyEvent.KEYCODE_BACK));
        testLaunch();
    }

    @Test
    public void testNavigateToNeedHelpResourcesActivity(){
        onView(withId(R.id.buttonHelpPageOther)).perform(scrollTo(), click());

        // Test learning resources activity launches and back again
        onView(withId(R.id.recyclerViewResources)).check(matches(isDisplayed())).perform(pressKey(KeyEvent.KEYCODE_BACK));
        testLaunch();
    }
}