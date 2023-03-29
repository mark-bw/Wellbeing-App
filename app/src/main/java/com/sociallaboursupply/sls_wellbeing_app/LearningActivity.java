package com.sociallaboursupply.sls_wellbeing_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.sociallaboursupply.sls_wellbeing_app.Utils.SubActivity;

public class LearningActivity extends SubActivity {

    Button coursesPageButton;
    Button resourcesPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        setTitle(R.string.learning_page_heading);

        // Courses page button
        coursesPageButton = (Button) findViewById(R.id.buttonLearningPageCourses);
        coursesPageButton.setOnClickListener(view -> startActivity(new Intent(LearningActivity.this, CoursesActivity.class)));

        // Resources page button
        resourcesPageButton = (Button) findViewById(R.id.buttonLearningPageResources);
        resourcesPageButton.setOnClickListener(view -> startActivity(new Intent(LearningActivity.this, LearningResourcesActivity.class)));
    }
}