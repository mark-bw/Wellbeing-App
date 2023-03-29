package com.sociallaboursupply.sls_wellbeing_app.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sociallaboursupply.sls_wellbeing_app.Model.TaskModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TasksDatabaseHandlerTest {

    DatabaseHandler dbh;
    TasksDatabaseHandler tasksDBh;
    TaskModel testTask;

    @Before
    public void setUp() {
        dbh = new DatabaseHandler(ApplicationProvider.getApplicationContext());
        tasksDBh = new TasksDatabaseHandler(ApplicationProvider.getApplicationContext());
        testTask = new TaskModel();
        testTask.setTitle("test");
        testTask.setDescription("desc");
    }

    @After
    public void tearDown() {
        dbh.close();
        ApplicationProvider.getApplicationContext().deleteDatabase("SLSDB");
    }

    @Test
    public void insertTask() {
        SQLiteDatabase db = dbh.openDatabase();
        Cursor cur = db.rawQuery("SELECT status, title, description FROM tasks", null);
        assertFalse(cur.moveToFirst());
        cur.close();
        tasksDBh.insertTask(testTask);
        cur = db.rawQuery("SELECT status, title, description FROM tasks", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getInt(0), 0);
        assertEquals(cur.getString(1), "test");
        assertEquals(cur.getString(2), "desc");
        assertFalse(cur.moveToNext());
        cur.close();
    }

    @Test
    public void getAllTasks() {
        List<TaskModel> tasks = tasksDBh.getAllTasks();
        assertTrue(tasks.isEmpty());
        populateTasksTable();
        tasks = tasksDBh.getAllTasks();
        assertFalse(tasks.isEmpty());
        assertEquals(tasks.get(0).getTitle(), "test1");
        assertEquals(tasks.get(1).getTitle(), "test2");
        assertEquals(tasks.get(2).getTitle(), "test3");
        assertEquals(tasks.get(0).getDescription(), "desc1");
        assertEquals(tasks.get(1).getDescription(), "desc2");
        assertEquals(tasks.get(2).getDescription(), "desc3");
    }

    @Test
    public void updateStatus() {
        populateTasksTable();
        SQLiteDatabase db = dbh.openDatabase();
        Cursor cur = db.rawQuery("SELECT status FROM tasks", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getInt(0), 0);
        cur.close();
        tasksDBh.updateStatus(1, 1);
        cur = db.rawQuery("SELECT status FROM tasks", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getInt(0), 1);
        cur.close();
    }

    @Test
    public void updateTask() {
        populateTasksTable();
        SQLiteDatabase db = dbh.openDatabase();
        Cursor cur = db.rawQuery("SELECT title, description FROM tasks", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "test1");
        assertEquals(cur.getString(1), "desc1");
        cur.close();
        tasksDBh.updateTask(1, "updated", "updated");
        cur = db.rawQuery("SELECT title, description FROM tasks", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "updated");
        assertEquals(cur.getString(1), "updated");
        cur.close();
    }

    @Test
    public void deleteTask() {
        populateTasksTable();
        SQLiteDatabase db = dbh.openDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM tasks", null);
        assertTrue(cur.moveToPosition(2));
        assertFalse(cur.moveToPosition(3));
        cur.close();
        tasksDBh.deleteTask(1);
        cur = db.rawQuery("SELECT * FROM tasks", null);
        assertTrue(cur.moveToPosition(1));
        assertFalse(cur.moveToPosition(2));
        cur.close();
    }

    private void populateTasksTable(){
        SQLiteDatabase db = dbh.openDatabase();
        db.execSQL("INSERT INTO tasks (status, title, description) VALUES " +
                "(0, 'test1', 'desc1')," +
                "(0, 'test2', 'desc2')," +
                "(0, 'test3', 'desc3')");
        db.close();
    }
}