package com.sociallaboursupply.sls_wellbeing_app.Database;

import static org.junit.Assert.*;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;

import com.sociallaboursupply.sls_wellbeing_app.Model.MoodModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MoodDatabaseHandlerTest {

    DatabaseHandler dbh;
    MoodDatabaseHandler moodDBh;
    MoodModel testMood;

    @Before
    public void setUp() {
        dbh = new DatabaseHandler(ApplicationProvider.getApplicationContext());
        moodDBh = new MoodDatabaseHandler(ApplicationProvider.getApplicationContext());
        testMood = new MoodModel();
        testMood.setStatus(MoodModel.HAPPY);
        testMood.setNote("test");
        testMood.setDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
    }

    @After
    public void tearDown() {
        dbh.close();
        ApplicationProvider.getApplicationContext().deleteDatabase("SLSDB");
    }

    @Test
    public void insert() {
        SQLiteDatabase db = dbh.openDatabase();
        Cursor cur = db.rawQuery("SELECT note, status, date FROM moods", null);
        assertFalse(cur.moveToFirst());
        cur.close();
        moodDBh.insert(testMood);
        cur = db.rawQuery("SELECT note, status, date FROM moods", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "test");
        assertEquals(cur.getString(1), "happy");
        assertEquals(cur.getString(2), "2020-01-01 00:00:00");
        assertFalse(cur.moveToNext());
        cur.close();
    }

    @Test
    public void getAllMoods() {
        List<MoodModel> moods = moodDBh.getAllMoods();
        assertTrue(moods.isEmpty());
        populateMoodTable();
        moods = moodDBh.getAllMoods();
        assertFalse(moods.isEmpty());
        assertEquals(moods.get(0).getNote(), "test1");
        assertEquals(moods.get(1).getNote(), "test2");
        assertEquals(moods.get(2).getNote(), "test3");
    }

    @Test
    public void updateMoodStatus() {
        populateMoodTable();
        SQLiteDatabase db = dbh.openDatabase();
        Cursor cur = db.rawQuery("SELECT status FROM moods", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "happy");
        cur.close();
        moodDBh.updateMoodStatus(1, "sad");
        cur = db.rawQuery("SELECT status FROM moods", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "sad");
        cur.close();
    }

    @Test
    public void updateMoodNote() {
        populateMoodTable();
        SQLiteDatabase db = dbh.openDatabase();
        Cursor cur = db.rawQuery("SELECT note FROM moods", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "test1");
        cur.close();
        moodDBh.updateMoodNote(1, "updated");
        cur = db.rawQuery("SELECT note FROM moods", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "updated");
        cur.close();
    }

    @Test
    public void updateMoodDetails() {
        populateMoodTable();
        SQLiteDatabase db = dbh.openDatabase();
        Cursor cur = db.rawQuery("SELECT status, note FROM moods", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "happy");
        assertEquals(cur.getString(1), "test1");
        cur.close();
        moodDBh.updateMoodDetails(1, "sad", "updated");
        cur = db.rawQuery("SELECT status, note FROM moods", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "sad");
        assertEquals(cur.getString(1), "updated");
        cur.close();
    }

    @Test
    public void deleteMood() {
        populateMoodTable();
        SQLiteDatabase db = dbh.openDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM moods", null);
        assertTrue(cur.moveToPosition(2));
        assertFalse(cur.moveToPosition(3));
        cur.close();
        moodDBh.deleteMood(1);
        cur = db.rawQuery("SELECT * FROM moods", null);
        assertTrue(cur.moveToPosition(1));
        assertFalse(cur.moveToPosition(2));
        cur.close();
    }

    private void populateMoodTable(){
        SQLiteDatabase db = dbh.openDatabase();
        db.execSQL("INSERT INTO moods (status, note, date) VALUES " +
                "('happy', 'test1', '2020-01-01 00:00:00')," +
                "('neutral', 'test2', '2020-01-01 00:00:00')," +
                "('sad', 'test3', '2020-01-01 00:00:00')");
        db.close();
    }
}