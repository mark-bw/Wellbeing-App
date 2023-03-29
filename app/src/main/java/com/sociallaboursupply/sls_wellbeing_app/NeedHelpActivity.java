package com.sociallaboursupply.sls_wellbeing_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sociallaboursupply.sls_wellbeing_app.Database.UserDatabaseHandler;
import com.sociallaboursupply.sls_wellbeing_app.Model.UserModel;
import com.sociallaboursupply.sls_wellbeing_app.Utils.SocialIntents;
import com.sociallaboursupply.sls_wellbeing_app.Utils.SubActivity;

public class NeedHelpActivity extends SubActivity {
    TextView mentorNameLabel;
    Button phoneButton;
    Button smsButton;
    Button emailButton;
    Button learningPageButton;
    Button mentalHealthPageButton;
    Button OtherPageButton;
    Button workPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_help);
        setTitle(R.string.button_need_help);

        int userId = PreferenceData.getUserID(this);
        UserDatabaseHandler udh = new UserDatabaseHandler(this);
        UserModel mentor = udh.getUsersMentor(userId);

        if (mentor != null) {
            setupMentor(mentor);
            setupPhoneSMS(mentor);
            setupEmail(mentor);
        } else {
            // show no mentor text
            View mentorBlock = findViewById(R.id.mentorBlock);
            mentorBlock.setVisibility(View.GONE);
            View noMentorBlock = findViewById(R.id.noMentorBlock);
            noMentorBlock.setVisibility(View.VISIBLE);
        }

        // Learning page button
        learningPageButton = (Button) findViewById(R.id.buttonHelpPageLearning);
        learningPageButton.setOnClickListener(view -> startActivity(new Intent(NeedHelpActivity.this, LearningActivity.class)));

        // Mental Health page button
        mentalHealthPageButton = (Button) findViewById(R.id.buttonHelpPageMental);
        mentalHealthPageButton.setOnClickListener(view -> startActivity(new Intent(NeedHelpActivity.this, MentalActivity.class)));

        // Other page button
        OtherPageButton = (Button) findViewById(R.id.buttonHelpPageOther);
        OtherPageButton.setOnClickListener(view -> startActivity(new Intent(NeedHelpActivity.this, NeedHelpResourcesActivity.class)));

        // work page button
        workPageButton = (Button) findViewById(R.id.buttonHelpPageWork);
        workPageButton.setOnClickListener((view -> startActivity(new Intent(NeedHelpActivity.this, MentorActivity.class))));
    }
    private void setupMentor(UserModel mentor) {
        // Set the name of the mentor
        mentorNameLabel = (TextView) findViewById(R.id.mentorName);
        mentorNameLabel.setText(mentor.getFirstName() + " " + mentor.getLastName());
    }

    private void setupPhoneSMS(UserModel mentor) {
        // Phone mentor button
        Context context = getApplicationContext();
        String phoneNumber = mentor.getPhone();

        Button phoneButton = (Button) findViewById(R.id.phoneButton);
        SocialIntents.setupPhoneButton(this, findViewById(R.id.scrollViewHelpPage), phoneButton, phoneNumber);

        // SMS mentor button
        Button smsButton = (Button) findViewById(R.id.smsButton);
        String smsMessage = "Hi " + mentor.getFirstName() + ", " +
                context.getResources().getString(R.string.help_send_email_body);
        SocialIntents.setupSMSButton(this, findViewById(R.id.scrollViewHelpPage), smsButton, phoneNumber, smsMessage);
    }

    private void setupEmail(UserModel mentor) {
        // Email mentor button
        Context context = getApplicationContext();

        Button emailButton = (Button) findViewById(R.id.emailButton);
        String emailAddress = mentor.getEmail();
        String emailSubject = context.getResources().getString(R.string.help_send_email_subject);
        String emailMessage = "Hi " + mentor.getFirstName() + ",\n\n" +
                context.getResources().getString(R.string.help_send_email_body);

        SocialIntents.setupEmailButton(this, findViewById(R.id.scrollViewHelpPage), emailButton, emailAddress, emailSubject, emailMessage);
    }
}