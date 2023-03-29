package com.sociallaboursupply.sls_wellbeing_app.Utils;

import com.sociallaboursupply.sls_wellbeing_app.Model.CourseModel;
import com.sociallaboursupply.sls_wellbeing_app.Model.MoodModel;
import com.sociallaboursupply.sls_wellbeing_app.Model.TaskModel;
import com.sociallaboursupply.sls_wellbeing_app.Model.UserModel;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DummyDataTest {

    @Test
    public void createDummyUser() {
        UserModel user = DummyData.createDummyUser();
        assertEquals(user.getEmail(), "user@example.com");
        assertEquals(user.getPassword(), "password");
    }

    @Test
    public void createDummyMoodData() {
        ArrayList<MoodModel> moods = DummyData.createDummyMoodData(1);
        assertFalse(moods.isEmpty());
    }

    @Test
    public void createDummyTaskData() {
        ArrayList<TaskModel> tasks = DummyData.createDummyTaskData(1);
        assertFalse(tasks.isEmpty());
    }

    @Test
    public void createDummyCourseData() {
        ArrayList<CourseModel> courses = DummyData.createDummyCourseData();
        assertFalse(courses.isEmpty());
    }
}