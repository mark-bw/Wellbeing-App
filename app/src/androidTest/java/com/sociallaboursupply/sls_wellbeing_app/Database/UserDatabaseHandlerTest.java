package com.sociallaboursupply.sls_wellbeing_app.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.sociallaboursupply.sls_wellbeing_app.Model.UserModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserDatabaseHandlerTest {

    DatabaseHandler dbh;
    UserDatabaseHandler userDBh;
    UserModel testUser;

    @Before
    public void setUp() {
        dbh = new DatabaseHandler(ApplicationProvider.getApplicationContext());
        userDBh = new UserDatabaseHandler(ApplicationProvider.getApplicationContext());
        testUser = new UserModel();
        testUser.setFirstName("testFirstName");
        testUser.setLastName("testLastName");
        testUser.setEmail("test@example.com");
        testUser.setPassword("testPassword");
        testUser.setMentor(false);
    }

    @After
    public void tearDown() {
        dbh.close();
        ApplicationProvider.getApplicationContext().deleteDatabase("SLSDB");
    }

    @Test
    public void insert() {
        SQLiteDatabase db = dbh.openDatabase();
        Cursor cur = db.rawQuery("SELECT first_name, last_name, email, password, is_mentor FROM users", null);
        assertFalse(cur.moveToFirst());
        cur.close();
        try {
            userDBh.insert(testUser);
        } catch (SQLiteConstraintException e) {
            throw e;
        }
        cur = db.rawQuery("SELECT first_name, last_name, email, password, is_mentor FROM users", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "testFirstName");
        assertEquals(cur.getString(1), "testLastName");
        assertEquals(cur.getString(2), "test@example.com");
        assertEquals(cur.getString(3), "testPassword");
        assertEquals(cur.getInt(4), 0);
        assertFalse(cur.moveToNext());
        cur.close();
    }

    @Test
    public void getAllAccounts() {
        List<UserModel> users = userDBh.getAllAccounts();
        assertTrue(users.isEmpty());
        populateUsersTable();
        users = userDBh.getAllAccounts();
        assertFalse(users.isEmpty());
        assertEquals(users.get(0).getFirstName(), "testUserFirstName1");
        assertEquals(users.get(1).getFirstName(), "testUserFirstName2");
        assertEquals(users.get(2).getFirstName(), "testMentorFirstName1");
        assertEquals(users.get(3).getFirstName(), "testMentorFirstName2");
    }

    // TODO: Implement test once method is fixed
//    @Test
//    public void getAllMentors() {
//        List<UserModel> users = userDBh.getAllAccounts();
//        assertTrue(users.isEmpty());
//        populateUsersTable();
//        users = userDBh.getAllMentors();
//        assertFalse(users.isEmpty());
//        assertEquals(users.get(0).getFirstName(), "testMentorFirstName1");
//        assertEquals(users.get(1).getFirstName(), "testMentorFirstName2");
//    }

    // TODO: Implement test once method is fixed
//    @Test
//    public void getAllUsers() {
//        List<UserModel> users = userDBh.getAllAccounts();
//        assertTrue(users.isEmpty());
//        populateUsersTable();
//        users = userDBh.getAllUsers();
//        assertFalse(users.isEmpty());
//        assertEquals(users.get(0).getFirstName(), "testUserFirstName1");
//        assertEquals(users.get(1).getFirstName(), "testUserFirstName2");
//    }

    @Test
    public void getUserWithEmailAndPassword() {
        List<UserModel> users = userDBh.getUserWithEmailAndPassword("testMentor1@example.com", "testMentorPassword1");
        assertTrue(users.isEmpty());
        populateUsersTable();
        users = userDBh.getUserWithEmailAndPassword("incorrectemail", "testMentorPassword1");
        assertTrue(users.isEmpty());
        users = userDBh.getUserWithEmailAndPassword("testMentor1@example.com", "incorrectPassword");
        assertTrue(users.isEmpty());
        users = userDBh.getUserWithEmailAndPassword("testMentor1@example.com", "testMentorPassword1");
        assertFalse(users.isEmpty());
        assertEquals(users.get(0).getFirstName(), "testMentorFirstName1");
    }

    @Test
    public void updatePassword() {
        populateUsersTable();
        SQLiteDatabase db = dbh.openDatabase();
        Cursor cur = db.rawQuery("SELECT password FROM users", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "testUserPassword1");
        cur.close();
        userDBh.updatePassword(1, "updated");
        cur = db.rawQuery("SELECT password FROM users", null);
        assertTrue(cur.moveToFirst());
        assertEquals(cur.getString(0), "updated");
        cur.close();
    }

    @Test
    public void deleteUser() {
        populateUsersTable();
        SQLiteDatabase db = dbh.openDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM users", null);
        assertTrue(cur.moveToPosition(3));
        assertFalse(cur.moveToPosition(4));
        cur.close();
        userDBh.deleteUser(1);
        cur = db.rawQuery("SELECT * FROM users", null);
        assertTrue(cur.moveToPosition(2));
        assertFalse(cur.moveToPosition(3));
        cur.close();
    }

    private void populateUsersTable(){
        SQLiteDatabase db = dbh.openDatabase();
        db.execSQL("INSERT INTO users (first_name, last_name, email, password, is_mentor) VALUES " +
                "('testUserFirstName1', 'testUserLastName1', 'testUser1@example.com', 'testUserPassword1', 0)," +
                "('testUserFirstName2', 'testUserLastName2', 'testUser2@example.com', 'testUserPassword2', 0)," +
                "('testMentorFirstName1', 'testMentorLastName1', 'testMentor1@example.com', 'testMentorPassword1', 1)," +
                "('testMentorFirstName2', 'testMentorLastName2', 'testMentor2@example.com', 'testMentorPassword2', 1)");
        db.close();
    }
}