package com.sociallaboursupply.sls_wellbeing_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button buttonMoodTracker;
    Button buttonNeedHelp;
    Button buttonTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mood Tracker button directs user to Mood Tracker Page
        buttonMoodTracker = findViewById(R.id.button_moodTracker);
        buttonMoodTracker.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MoodTrackerActivity.class)));

        // Need Help button directs user to Need Help Page
        buttonNeedHelp = findViewById(R.id.button_help);
        buttonNeedHelp.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, NeedHelpActivity.class)));

        // Task button directs user to Tasks Page
        buttonTasks = findViewById(R.id.button_tasks);
        buttonTasks.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TaskActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout_menu) {
            PreferenceData.clearPreferences(this);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.scale_fade_in, 0);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}