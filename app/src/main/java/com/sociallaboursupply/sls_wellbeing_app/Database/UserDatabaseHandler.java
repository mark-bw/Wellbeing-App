package com.sociallaboursupply.sls_wellbeing_app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.sociallaboursupply.sls_wellbeing_app.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHandler {

    public static final String USERS_TABLE = "users";
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String IS_MENTOR = "is_mentor";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String MENTOR_ID = "mentor_id";
    private static final String PHONE = "phone";

    // NOTE: Creates the user table
    public static final String CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + USERS_TABLE +
            "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EMAIL + " TEXT UNIQUE, " +
                PASSWORD + " TEXT, " +
                IS_MENTOR + " INTEGER, " +
                FIRST_NAME + " TEXT, " +
                LAST_NAME + " TEXT, " +
                MENTOR_ID + " INTEGER, " +
                PHONE + " TEXT " +
            ")";

    private SQLiteDatabase db;

    public UserDatabaseHandler(Context context) {
        DatabaseHandler dbh = new DatabaseHandler(context);
        db = dbh.openDatabase();
    }

    public void insert(UserModel user) throws SQLiteConstraintException {
        ContentValues cv = getContentValues(user);
        try {
            db.insertOrThrow(USERS_TABLE, null, cv);
        } catch (SQLiteConstraintException e) {
            throw e;
        }
    }

    // Retrieves the mentor for a user by the user's id and mentor_id
    public UserModel getUsersMentor(int userId){
        Cursor cursor = null;
        db.beginTransaction();
        UserModel user = null;
        try{
            // subquery gets the mentor id for the current user
            String subquery = String.format("SELECT %s FROM %s WHERE %s = %s", MENTOR_ID, USERS_TABLE, ID, userId);
            // query gets the user data for the user with the mentor id
            String query = String.format("SELECT * FROM %s WHERE %s = (%s)", USERS_TABLE, ID, subquery);
            cursor = db.rawQuery(query, null);
            List<UserModel> users = processUserList(cursor);
            if (!users.isEmpty()) {
                user = users.get(0);
            }
        }
        finally {
            db.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return user;
    }

    // Returns a list of UserModel objects for each row in the database
    public List<UserModel> getAllAccounts(){
        return getUserList(null, null);
    }

    // Returns a list of all mentor UserModel objects for each row in the database
    public List<UserModel> getAllMentors(){
        return getUserList("is_mentor=?", new String[]{"1"});
    }
    // Returns a list of all users that are mentors except the selected one
    public List<UserModel> getAllOtherMentors(int mentorID){
        String mid = String.valueOf(mentorID);
        return getUserList("is_mentor=? AND id!=?", new String[]{"1", mid});
    }

    public List<UserModel> getAllUsers(){
        return getUserList("is_mentor=?", new String[]{"0"});
    }

    private List<UserModel> getUserList(String selection, String[] selectionArgs) {
        List<UserModel> userList = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
        try{
            cursor = db.query(USERS_TABLE, null, selection, selectionArgs, null, null, null, null);
            userList = processUserList(cursor);
        }
        finally {
            db.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return userList;
    }

    public UserModel getUserById(int userId) {
        List<UserModel> userList = getUserList(ID + "=?", new String[] {String.valueOf(userId)});
        UserModel user = null;
        if (!userList.isEmpty()) user = userList.get(0);
        return user;
    }

    public List<UserModel> getUserWithEmailAndPassword(String email, String password){
        List<UserModel> user;
        Cursor cursor= null;
        db.beginTransaction();
        try{
            cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE %s ='%s' AND %s = '%s'", USERS_TABLE, EMAIL, email, PASSWORD, password), null);
            user = processUserList(cursor);
        } finally {
            db.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return user;
    }

    private List<UserModel> processUserList(Cursor cursor) {
        List<UserModel> userList = new ArrayList<>();
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    UserModel user = processUser(cursor);
                    userList.add(user);
                }
                while(cursor.moveToNext());
            }
        }
        return userList;
    }

    private UserModel processUser(Cursor cursor) {
        UserModel user = new UserModel();
        user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
        Integer mentorStatus = cursor.getInt(cursor.getColumnIndex(IS_MENTOR));
        boolean isMentor = (mentorStatus != 0 && mentorStatus != null) ? true : false;
        user.setMentor(isMentor);
        user.setFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
        user.setLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
        user.setMentorId(cursor.getInt(cursor.getColumnIndex(MENTOR_ID)));
        user.setPhone(cursor.getString(cursor.getColumnIndex(PHONE)));
        return user;
    }

    // takes in the row id and status value and updates the status of that row in the table
    public void updatePassword(int id, String password){
        ContentValues cv = new ContentValues();
        cv.put(PASSWORD, password);
        db.update(USERS_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    // takes in the row id and removes row from the table
    public void deleteUser(int id){
        db.delete(USERS_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }

    // update user entry
    public void update(UserModel user) throws SQLiteConstraintException {
        ContentValues cv = getContentValues(user);
        db.update(USERS_TABLE, cv, ID + "= ?", new String[] {String.valueOf(user.getId())});
    }

    private ContentValues getContentValues(UserModel user) {
        ContentValues cv = new ContentValues();
        cv.put(EMAIL, user.getEmail());
        cv.put(PASSWORD, user.getPassword());
        int isMentor = user.isMentor ? 1 : 0;
        cv.put(IS_MENTOR, isMentor);
        cv.put(FIRST_NAME, user.getFirstName());
        cv.put(LAST_NAME, user.getLastName());
        cv.put(MENTOR_ID, user.getMentorId());
        cv.put(PHONE, user.getPhone());
        return cv;
    }
}
