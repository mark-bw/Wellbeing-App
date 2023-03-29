package com.sociallaboursupply.sls_wellbeing_app.Database;

import static org.junit.Assert.*;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseHandlerTest {

    private DatabaseHandler dbh;

    @Before
    public void setUp() {
        dbh = new DatabaseHandler(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() {
        dbh.close();
        ApplicationProvider.getApplicationContext().deleteDatabase("SLSDB");
    }

    @Test
    public void openDatabase() {
        SQLiteDatabase db = dbh.openDatabase();
        assertTrue(db.isOpen());
        db.close();
        assertFalse(db.isOpen());
    }

    @Test
    public void onCreate() {
        Cursor cur = dbh.openDatabase().rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT IN ('sqlite_sequence', 'android_metadata') order by name", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "moods");
        assertTrue(cur.moveToNext());
        assertEquals(cur.getString(0), "tasks");
        assertTrue(cur.moveToNext());
        assertEquals(cur.getString(0), "users");
        assertFalse(cur.moveToNext());
        cur.close();
    }

    @Test
    public void onUpgrade() {
        SQLiteDatabase db = dbh.openDatabase();
        db.execSQL("INSERT INTO users (email) VALUES ('test')");
        Cursor cur = db.rawQuery("SELECT email FROM users", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "test");
        assertFalse(cur.moveToNext());
        cur.close();
        dbh.onUpgrade(db, 1, 2);
        cur = db.rawQuery("SELECT email FROM users", null);
        assertFalse(cur.moveToNext());
    }
}