package com.sociallaboursupply.sls_wellbeing_app;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.EspressoKey;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.database.sqlite.SQLiteDatabase;
import android.view.KeyEvent;

import com.sociallaboursupply.sls_wellbeing_app.Database.DatabaseHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {

    SQLiteDatabase db;

    @Before
    public void setUp() {
        PreferenceData.clearPreferences(ApplicationProvider.getApplicationContext());

        // add a user and mentor to database for testing
        db = new DatabaseHandler(ApplicationProvider.getApplicationContext()).openDatabase();
        db.execSQL("INSERT INTO users (first_name, last_name, email, password, is_mentor) VALUES " +
                "('testUserFirstName', 'testUserLastName', 'testUser@example.com', 'testUserPassword', 0)," +
                "('testMentorFirstName', 'testMentorLastName', 'testMentor@example.com', 'testMentorPassword', 1)");
    }

    @After
    public void tearDown() {
        db.close();
        ApplicationProvider.getApplicationContext().deleteDatabase("SLSDB");
        PreferenceData.clearPreferences(ApplicationProvider.getApplicationContext());
    }

    // This rule starts the specified activity
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule= new ActivityScenarioRule<>(LoginActivity.class);

    // Find login button to test that the login page has launched
    @Test
    public void testLaunch(){
        onView(withId(R.id.editTextEmail)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginAsUserLaunchesMainActivity(){
        // Type correct email and password and click login
        onView(withId(R.id.editTextEmail)).perform(typeText("testUser@example.com"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.editTextPassword)).perform(typeText("testUserPassword"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.buttonLogin)).perform(click());

        // Check activity is now MainActivity
        onView(withId(R.id.button_moodTracker)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginAsMentorLaunchesMainActivity(){
        // Type correct email and password and click login
        onView(withId(R.id.editTextEmail)).perform(typeText("testMentor@example.com"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.editTextPassword)).perform(typeText("testMentorPassword"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.buttonLogin)).perform(click());

        // Check activity is now MainMentorActivity
        onView(withId(R.id.userRecyclerMentor)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithIncorrectEmailCorrectPassword(){
        // Type incorrect email and click login
        onView(withId(R.id.editTextEmail)).perform(typeText("IncorrectEmail"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.editTextPassword)).perform(typeText("testUserPassword"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.buttonLogin)).perform(click());

        // Check that activity is still LoginActivity
        onView(withId(R.id.editTextEmail)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithCorrectEmailIncorrectPassword(){
        // Type incorrect email and click login
        onView(withId(R.id.editTextEmail)).perform(typeText("testMentor@example.com"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.editTextPassword)).perform(typeText("incorrectPassword"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.buttonLogin)).perform(click());

        // Check that activity is still LoginActivity
        onView(withId(R.id.editTextEmail)).check(matches(isDisplayed()));
    }
}