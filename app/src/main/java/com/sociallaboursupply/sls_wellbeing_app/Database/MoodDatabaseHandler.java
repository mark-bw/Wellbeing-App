package com.sociallaboursupply.sls_wellbeing_app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sociallaboursupply.sls_wellbeing_app.Model.MoodModel;
import com.sociallaboursupply.sls_wellbeing_app.Utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MoodDatabaseHandler {

    private static final int VERSION = 1;
    public static final String MOOD_TABLE = "moods";

    private static final String ID = "id";
    private static final String USER_ID = "user_id";
    private static final String STATUS = "status";
    private static final String DATE = "date";
    private static final String NOTE = "note";

    // NOTE: Creates the mood table, adds status and date columns.  Status have restricted values
    public static final String CREATE_MOODS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + MOOD_TABLE +
            "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_ID + " INTEGER, " +
                STATUS + " TEXT check(" + STATUS + " = \"" + MoodModel.HAPPY + "\" or " +
                    STATUS + " = \"" + MoodModel.NEUTRAL + "\" or " +
                    STATUS + " = \"" + MoodModel.SAD + "\" ), " +
                DATE + " TEXT, " +
                NOTE + " TEXT" +
            ")";

    private SQLiteDatabase db;

    public MoodDatabaseHandler(Context context) {
        DatabaseHandler dbh = new DatabaseHandler(context);
        db = dbh.openDatabase();
    }

    public void insert(MoodModel mood){
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, mood.getUserId());
        cv.put(STATUS, mood.getStatus());
        cv.put(DATE, DateUtils.dateToDatabaseString(mood.getDate()));
        cv.put(NOTE, mood.getNote());
        db.insert(MOOD_TABLE, null, cv);
    }


    // Returns a list of MoodModel objects for each row in the database
    public List<MoodModel> getAllMoods(){
        return getMoodList(null, null);
    }

    // Get all the moods for a given user id
    public List<MoodModel> getUserMoods(int userId) {
        return getMoodList(USER_ID + "=?", new String[] {String.valueOf(userId)});
    }

    // takes in the row id and status value and updates the status of that row in the table
    public void updateMoodStatus(int id, String status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(MOOD_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    // takes in the row id and note value and updates the note of that row in the table
    public void updateMoodNote(int id, String note){
        ContentValues cv = new ContentValues();
        cv.put(NOTE, note);
        db.update(MOOD_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    // takes in the row id and status and note values and updates that row in the table
    public void updateMoodDetails(int id, String status, String note){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        cv.put(NOTE, note);
        db.update(MOOD_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    // takes in the row id and removes row from the table
    public void deleteMood(int id){
        db.delete(MOOD_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }


    // Helper function to get list of moods with some parameters
    private List<MoodModel> getMoodList(String selection, String[] selectionArgs) {
        List<MoodModel> moodList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(MOOD_TABLE, null, selection, selectionArgs, null, null, null, null);
            moodList = processMoodList(cur);
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return moodList;
    }
    // Helper function to assign cursor data to a MoodModel
    private List<MoodModel> processMoodList(Cursor cursor) {
        List<MoodModel> moodList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    MoodModel mood = new MoodModel();
                    mood.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    mood.setUserId(cursor.getInt(cursor.getColumnIndex(USER_ID)));
                    mood.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                    String moodDate = cursor.getString(cursor.getColumnIndex(DATE));
                    mood.setDate(DateUtils.databaseStringToDate(moodDate));
                    mood.setNote(cursor.getString(cursor.getColumnIndex((NOTE))));
                    moodList.add(mood);
                }
                while (cursor.moveToNext());
            }
        }
        return moodList;
    }
}
