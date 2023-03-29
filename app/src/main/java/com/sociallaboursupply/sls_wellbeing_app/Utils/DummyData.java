package com.sociallaboursupply.sls_wellbeing_app.Utils;

import com.sociallaboursupply.sls_wellbeing_app.Model.CourseModel;
import com.sociallaboursupply.sls_wellbeing_app.Model.MoodModel;
import com.sociallaboursupply.sls_wellbeing_app.Model.TaskModel;
import com.sociallaboursupply.sls_wellbeing_app.Model.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class DummyData {

    public static UserModel createDummyMentor() {
        UserModel mentor = new UserModel();
        mentor.setFirstName("Aroha");
        mentor.setLastName("Waititi");
        mentor.setPhone("91197253");
        mentor.setEmail("aroha@example.com");
        mentor.setPassword("aroha");
        mentor.setMentor(true);
        return mentor;
    }

    // returns a user model object
    public static UserModel createDummyUser(){
        UserModel user = new UserModel();
        user.setEmail("user@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setMentor(false);
        user.setMentorId(1);
        return user;
    }

    // returns a list of 10 sample moods with random status
    public static ArrayList<MoodModel> createDummyMoodData(int userId){
        ArrayList<MoodModel> moods = new ArrayList<>();
        Random rand = new Random();
        for(int i=0; i<10;i++){
            MoodModel mood = new MoodModel();
            mood.setUserId(userId);
            List<String> moodStatus = Arrays.asList(MoodModel.HAPPY, MoodModel.NEUTRAL, MoodModel.SAD);
            List<String> moodNote = Arrays.asList("Feeling great", "Feeling ok", "Feeling sad");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -i);
            int randInt = rand.nextInt(moodStatus.size());
            mood.setStatus(moodStatus.get(randInt));
            mood.setNote(moodNote.get(randInt));
            mood.setDate(cal.getTime());
            moods.add(mood);
        }
        Collections.reverse(moods);
        return moods;
    }

    // returns a list of 10 sample tasks
    public static ArrayList<TaskModel> createDummyTaskData(int userId){
        String[] sampleTaskTitles = {
                "Record your mood", "Write in your journal", "Contact your mentor", "Message your family",
                "Do the first course", "Check out our resources page", "See our mental health resources page",
                "Schedule mentor meeting", "Make your gym plan", "Clean your house"
        };
        ArrayList<TaskModel> tasks = new ArrayList<>();
        // returns a list of 10 tasks
        for(int i = 0; i < sampleTaskTitles.length; i++) {
            TaskModel exampleTask = new TaskModel();
            exampleTask.setTitle(sampleTaskTitles[i]);
            exampleTask.setUserId(userId);
            exampleTask.setStatus(0);
            tasks.add(exampleTask);
        }
        Collections.reverse(tasks);
        return tasks;
    }

    // returns a list of 3 sample courses
    public static ArrayList<CourseModel> createDummyCourseData(){
        ArrayList<CourseModel> courses = new ArrayList<>();
        for(int i =1; i<4; i++){
            CourseModel course = new CourseModel();
            course.setTitle("Course #" + i);
            course.setDescription("This is an example description for course " + i);
            courses.add(course);
        }
        return courses;
    }
}