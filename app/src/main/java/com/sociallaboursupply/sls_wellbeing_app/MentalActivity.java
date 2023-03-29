package com.sociallaboursupply.sls_wellbeing_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.sociallaboursupply.sls_wellbeing_app.Utils.SubActivity;

public class MentalActivity extends SubActivity {

    Button otherResourcesPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental);
        setTitle(R.string.mental_page_header);

        // Resources page button
        otherResourcesPageButton = (Button) findViewById(R.id.buttonMentalPageOtherResources);
        otherResourcesPageButton.setOnClickListener(view -> startActivity(new Intent(MentalActivity.this, MentalHealthResourcesActivity.class)));
    }
}