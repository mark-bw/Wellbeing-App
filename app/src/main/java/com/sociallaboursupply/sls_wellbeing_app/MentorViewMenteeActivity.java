package com.sociallaboursupply.sls_wellbeing_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.snackbar.Snackbar;
import com.sociallaboursupply.sls_wellbeing_app.Adapter.MentorTaskAdapter;
import com.sociallaboursupply.sls_wellbeing_app.Database.MoodDatabaseHandler;
import com.sociallaboursupply.sls_wellbeing_app.Database.TasksDatabaseHandler;
import com.sociallaboursupply.sls_wellbeing_app.Database.UserDatabaseHandler;
import com.sociallaboursupply.sls_wellbeing_app.Model.MoodModel;
import com.sociallaboursupply.sls_wellbeing_app.Model.TaskModel;
import com.sociallaboursupply.sls_wellbeing_app.Model.UserModel;
import com.sociallaboursupply.sls_wellbeing_app.Utils.MoodChart;
import com.sociallaboursupply.sls_wellbeing_app.Utils.SocialIntents;
import com.sociallaboursupply.sls_wellbeing_app.Utils.SubActivity;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MentorViewMenteeActivity extends SubActivity {

    private TasksDatabaseHandler tdh;
    private MoodDatabaseHandler mdh;
    private TaskModel selectedTask;
    private int selectedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_view_mentee);
        setTitle("User Profile");


        // Retrieving parameters passed to activity via https://stackoverflow.com/a/3913720
        Bundle b = getIntent().getExtras();
        Integer value = 0;
        if(b != null)
            value = b.getInt("userid");

        if (value > 0) {
            selectedUserId = value;
            tdh = new TasksDatabaseHandler(this);
            mdh = new MoodDatabaseHandler(this);
            UserDatabaseHandler udh = new UserDatabaseHandler(this);
            UserModel user = udh.getUserById(value);
            setupView(user);
        } else {
            setupView(null);
        }
    }
    private void setupView(UserModel user) {
        if (user != null) {
            loadUserData(user);
            Button addTaskButton = (Button) findViewById(R.id.addTaskButton);
            addTaskButton.setOnClickListener(v -> createTask());
        } else {
            findViewById(R.id.userCard).setVisibility(View.GONE);
            findViewById(R.id.moodChartCard).setVisibility(View.GONE);
            findViewById(R.id.taskListCard).setVisibility(View.GONE);
            findViewById(R.id.addTaskButton).setVisibility(View.GONE);
            setTitle("User not found");
        }
    }

    private void loadUserData(UserModel user) {
        setupUserProfile(user);

        List<MoodModel> moods = mdh.getUserMoods(user.getId());
        LineChart lineChart = findViewById(R.id.chartMoodTracker);
        MoodChart.drawChart(moods, lineChart, this);

        reloadTaskList();
    }

    private void reloadTaskList() {
        // Set RecyclerView for tasks to be a linearlayout
        RecyclerView tasksRecyclerView = findViewById(R.id.recyclerViewTasksList);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and bind adapter to recyclerview
        MentorTaskAdapter taskAdapter = new MentorTaskAdapter(this);
        tasksRecyclerView.setAdapter(taskAdapter);

        // Get tasks from database
        List<TaskModel> tasks = tdh.getAllUserTasks(selectedUserId);

        // If there are no tasks display a message
        //TODO
//        View emptyView = findViewById(R.id.textViewEmptyTaskList);
//        if(tasks.isEmpty()){
//            emptyView.setVisibility(View.VISIBLE);
//        } else {
//            emptyView.setVisibility(View.GONE);
//        }

        // Pass tasks onto the adapter to be displayed
        Collections.reverse(tasks);
        taskAdapter.setTasks(tasks);
        ((MentorTaskAdapter) taskAdapter).setCallback(new MentorTaskAdapter.Callback() {
            @Override
            public void onEditTask(TaskModel task) {
                editTask(task);
            }
            @Override
            public void onDeleteTask(TaskModel task) {
                deleteTask(task);
            }
        });
    }

    private void setupUserProfile(UserModel user) {
        TextView userNameLabel = (TextView) findViewById(R.id.userName);
        userNameLabel.setText(user.getFirstName() + " " + user.getLastName());

        View scrollView = findViewById(R.id.mentorViewMentee);

        // Email user button
        Button emailButton = (Button) findViewById(R.id.emailButton);
        String emailAddress = user.getEmail();
        SocialIntents.setupEmailButton(this, scrollView, emailButton, emailAddress);

        // Call user button
        Button phoneButton = (Button) findViewById(R.id.phoneButton);
        String phoneNumber = user.getPhone();
        SocialIntents.setupPhoneButton(this, scrollView, phoneButton, phoneNumber);

        // SMS user button
        Button smsButton = (Button) findViewById(R.id.smsButton);
        SocialIntents.setupSMSButton(this, scrollView, smsButton, phoneNumber);
    }

    private void openTaskDialog(TaskModel task) {
        boolean isEditing = task != null;
        if (task == null) {
            task = new TaskModel();
        }
        selectedTask = task;

        // Used to create the alert dialog box
        LayoutInflater li = LayoutInflater.from(this);
        // promptsView uses the mood_dialog layout
        View promptsView = li.inflate(R.layout.add_task_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        EditText titleInput = (EditText) promptsView.findViewById(R.id.addTaskTitleEditText);
        EditText descInput = (EditText) promptsView.findViewById(R.id.addTaskDescEditText);

        if (isEditing) {
            titleInput.setText(task.getTitle());
            descInput.setText(task.getDescription());
        }

        // Create the alert and configure the save and cancel buttons
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getText(R.string.general_save),
                        (dialog, id) -> {
                            // Save the user response
                            String taskTitle = titleInput.getText().toString();
                            String taskDesc = descInput.getText().toString();
                            if (isEditing) {
                                updateTaskEntry(taskTitle, taskDesc);
                            } else {
                                createTaskEntry(taskTitle, taskDesc);
                            }
                        })
                .setNegativeButton(getText(R.string.general_cancel),
                        (dialog, id) -> dialog.cancel());

        // Create and open the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createTask() {
        openTaskDialog(null);
    }
    private void editTask(TaskModel task) {
        openTaskDialog(task);
    }
    private void deleteTask(TaskModel task) {
        new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("Are you sure you want to delete this task? (This cannot be undone)")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteTaskEntry(task);
                    }})
                .setNegativeButton(R.string.general_cancel, null).show();
    }
    private void updateTaskEntry(String title, String desc) {
        tdh.updateTask(selectedTask.getId(), title, desc);
        Snackbar.make(findViewById(R.id.mentorViewMentee),
                getText(R.string.general_saved), Snackbar.LENGTH_SHORT).show();
        reloadTaskList();
    }
    private void createTaskEntry(String title, String desc) {
        TaskModel task = new TaskModel();
        task.setTitle(title);
        task.setDescription(desc);
        task.setUserId(selectedUserId);
        tdh.insertTask(task);
        Snackbar.make(findViewById(R.id.mentorViewMentee),
                getText(R.string.general_saved), Snackbar.LENGTH_SHORT).show();
        reloadTaskList();
    }
    private void deleteTaskEntry(TaskModel task) {
        tdh.deleteTask(task.getId());
        Snackbar.make(findViewById(R.id.mentorViewMentee),
                "Deleted", Snackbar.LENGTH_SHORT).show();
        reloadTaskList();
    }
}