package com.sociallaboursupply.sls_wellbeing_app.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "SLSDB";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    public SQLiteDatabase openDatabase() {
        db = this.getWritableDatabase();
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the tables
        db.execSQL(UserDatabaseHandler.CREATE_USERS_TABLE);
        db.execSQL(MoodDatabaseHandler.CREATE_MOODS_TABLE);
        db.execSQL(TasksDatabaseHandler.CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL("DROP TABLE IF EXISTS " + UserDatabaseHandler.USERS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + MoodDatabaseHandler.MOOD_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TasksDatabaseHandler.TASKS_TABLE);
            onCreate(db);
        }
    }

}
