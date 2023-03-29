package com.sociallaboursupply.sls_wellbeing_app;

import android.os.Bundle;
import android.view.View;

import com.sociallaboursupply.sls_wellbeing_app.Adapter.TaskAdapter;
import com.sociallaboursupply.sls_wellbeing_app.Model.TaskModel;
import com.sociallaboursupply.sls_wellbeing_app.Utils.DummyData;
import com.sociallaboursupply.sls_wellbeing_app.Utils.SubActivity;
import com.sociallaboursupply.sls_wellbeing_app.Database.TasksDatabaseHandler;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TaskActivity extends SubActivity {

    private TasksDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setTitle(R.string.tasks_page_header);

        // Creates table in the database for tasks if one does not exists, and opens database
        db = new TasksDatabaseHandler(this);

        // insert dummy mood data into database if database is empty
        if(db.getAllUserTasks(PreferenceData.getUserID(this)).isEmpty()) {
            int userId = PreferenceData.getUserID(this);
            for(TaskModel task : DummyData.createDummyTaskData(userId)){
                db.insertTask(task);
            }
        }

        // create list to hold task objects retrieved from database
        List<TaskModel> taskList;

        // Set RecyclerView for tasks to be a linearlayout
        RecyclerView tasksRecyclerView = findViewById(R.id.recyclerViewTasksList);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and bind adapter to recyclerview
        TaskAdapter taskAdapter = new TaskAdapter(TaskActivity.this);
        tasksRecyclerView.setAdapter(taskAdapter);

        // Get tasks from database
        taskList = db.getAllUserTasks(PreferenceData.getUserID(this));

        // If there are no tasks display a message
        View emptyView = findViewById(R.id.textViewEmptyTaskList);
        if(taskList.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }

        // Pass tasks onto the adapter to be displayed
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);
    }

    // Change the status of a task.  Called by TaskAdapter
    public void updateTaskStatus(int rowId, int status) {
        db.updateStatus(rowId, status);
    }
}