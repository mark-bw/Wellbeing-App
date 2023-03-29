package com.sociallaboursupply.sls_wellbeing_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.KeyEvent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sociallaboursupply.sls_wellbeing_app.Database.DatabaseHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AdminActivityTest {

    SQLiteDatabase db;

    @Before
    public void setUp() {
        PreferenceData.clearPreferences(ApplicationProvider.getApplicationContext());

        // add a user and mentor to database for testing
        db = new DatabaseHandler(ApplicationProvider.getApplicationContext()).openDatabase();
        db.execSQL("DELETE FROM users");
    }

    @After
    public void tearDown() {
        db.close();
        ApplicationProvider.getApplicationContext().deleteDatabase("SLSDB");
        PreferenceData.clearPreferences(ApplicationProvider.getApplicationContext());
    }

    // This rule starts the specified activity
    @Rule
    public ActivityScenarioRule<AdminActivity> activityRule= new ActivityScenarioRule<>(AdminActivity.class);

    // Find User Recycler View to test that the admin page has launched
    @Test
    public void testLaunch(){
        onView(withId(R.id.userRecycler)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddUser() {

        // input user info into form
        onView(withId(R.id.addUserButton)).perform(click());
        onView(withId(R.id.firstNameInput)).perform(typeText("Test"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.lastNameInput)).perform(typeText("User"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.emailInput)).perform(typeText("test@example.com"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.passwordInput)).perform(typeText("testPassword"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.phoneInput)).perform(typeText("027027027"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.mentorCheckbox)).perform(scrollTo(), click());
        onView(withText("SAVE")).perform(click());

        // query database and check first entry values are correct
        Cursor cur = db.rawQuery("SELECT first_name, last_name, email, password, phone, is_mentor FROM users", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "Test");
        assertEquals(cur.getString(1), "User");
        assertEquals(cur.getString(2), "test@example.com");
        assertEquals(cur.getString(3), "testPassword");
        assertEquals(cur.getString(4), "027027027");
        assertEquals(cur.getInt(5), 1);
        assertFalse(cur.moveToNext());
        cur.close();
    }

    @Test
    public void testEditUser() {
        // add user to database
        db.execSQL("INSERT INTO users (first_name, last_name, email, password, phone, is_mentor) VALUES " +
                "('Test', 'User', 'test@example.com', 'testPassword', '027027027', 0)");

        // query database and check first entry values are correct
        Cursor cur = db.rawQuery("SELECT first_name, last_name, email, password, phone, is_mentor FROM users", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "Test");
        assertEquals(cur.getString(1), "User");
        assertEquals(cur.getString(2), "test@example.com");
        assertEquals(cur.getString(3), "testPassword");
        assertEquals(cur.getString(4), "027027027");
        assertEquals(cur.getInt(5), 0);
        assertFalse(cur.moveToNext());
        cur.close();

        // refresh activity
        activityRule.getScenario().recreate();

        // change user info in form
        onView(withText("Test User")).perform(click());
        onView(withId(R.id.firstNameInput)).perform(clearText(), typeText("UpdatedFirstName"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.lastNameInput)).perform(clearText(), typeText("UpdatedLaseName"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.passwordInput)).perform(clearText(), typeText("updatedPassword"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.phoneInput)).perform(clearText(), typeText("02700000"), pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.mentorCheckbox)).perform(scrollTo(), click());
        onView(withText("SAVE")).perform(click());

        // query database and check first entry values are correct
        cur = db.rawQuery("SELECT first_name, last_name, email, password, phone, is_mentor FROM users", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "UpdatedFirstName");
        assertEquals(cur.getString(1), "UpdatedLaseName");
        assertEquals(cur.getString(2), "test@example.com");
        assertEquals(cur.getString(3), "updatedPassword");
        assertEquals(cur.getString(4), "02700000");
        assertEquals(cur.getInt(5), 1);
        assertFalse(cur.moveToNext());
        cur.close();
    }
}