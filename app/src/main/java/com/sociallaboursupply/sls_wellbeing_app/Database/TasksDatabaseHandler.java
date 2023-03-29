package com.sociallaboursupply.sls_wellbeing_app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sociallaboursupply.sls_wellbeing_app.Model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TasksDatabaseHandler {

    private static final int VERSION = 1;
    private static final String NAME = "taskListDatabase";
    public static final String TASKS_TABLE = "tasks";
    private static final String ID = "id";
    private static final String USER_ID = "user_id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String STATUS = "status";
    public static final String CREATE_TASKS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TASKS_TABLE + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_ID + " INTEGER, " +
                    TITLE + " TEXT, " +
                    DESCRIPTION + " TEXT, " +
                    STATUS + " INTEGER" +
            ")";

    private SQLiteDatabase db;

    public TasksDatabaseHandler(Context context) {
        DatabaseHandler dbh = new DatabaseHandler(context);
        db = dbh.openDatabase();
    }

    public void insertTask(TaskModel task){
        ContentValues cv = new ContentValues();
        cv.put(TITLE, task.getTitle());
        cv.put(DESCRIPTION, task.getDescription());
        cv.put(USER_ID, task.getUserId());
        cv.put(STATUS, 0);
        db.insert(TASKS_TABLE, null, cv);
    }

    // Returns a list of TaskModel objects for each row in the database
    public List<TaskModel> getAllTasks(){
        return getTaskList(null, null);
    }
    // Returns a list of TaskModel objects for a user in the database
    public List<TaskModel> getAllUserTasks(int userId){
        String userIdString = String.valueOf(userId);
        return getTaskList(USER_ID + " = ?", new String[] {userIdString});
    }

    // takes in the row id and status value and updates the status of that row in the table
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TASKS_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    // takes in the row id and task string and updates the status of that row in the table
    public void updateTask(int id, String title, String description) {
        ContentValues cv = new ContentValues();
        cv.put(TITLE, title);
        cv.put(DESCRIPTION, description);
        db.update(TASKS_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    // takes in the row id and removes row from the table
    public void deleteTask(int id){
        db.delete(TASKS_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }

    private List<TaskModel> getTaskList(String selection, String[] selectionArgs) {
        List<TaskModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TASKS_TABLE, null, selection, selectionArgs, null, null, null, null);
            taskList = processTaskList(cur);
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    private List<TaskModel> processTaskList(Cursor cur) {
        List<TaskModel> taskList = new ArrayList<>();
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    TaskModel task = new TaskModel();
                    task.setId(cur.getInt(cur.getColumnIndex(ID)));
                    task.setUserId(cur.getInt(cur.getColumnIndex(USER_ID)));
                    task.setTitle(cur.getString(cur.getColumnIndex(TITLE)));
                    task.setDescription(cur.getString(cur.getColumnIndex(DESCRIPTION)));
                    task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                    taskList.add(task);
                }
                while (cur.moveToNext());
            }
        }
        return taskList;
    }
}
