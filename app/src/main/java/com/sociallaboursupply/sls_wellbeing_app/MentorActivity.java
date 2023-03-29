package com.sociallaboursupply.sls_wellbeing_app;

import android.os.Bundle;
import android.widget.TextView;

import com.sociallaboursupply.sls_wellbeing_app.Database.UserDatabaseHandler;
import com.sociallaboursupply.sls_wellbeing_app.Model.UserModel;
import com.sociallaboursupply.sls_wellbeing_app.Utils.SubActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MentorActivity extends SubActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);
        setTitle(getString(R.string.Mentor_Page_Heading));

        TextView textViewDate = findViewById(R.id.textViewCatchUpDate);
        setDate(textViewDate);
        setMentor();
    }

    private void setMentor() {
        UserDatabaseHandler udh = new UserDatabaseHandler(this);
        UserModel mentor = udh.getUsersMentor(PreferenceData.getUserID(this));
        TextView textViewMentorName = (TextView) findViewById(R.id.textViewMentorName);
        if (mentor == null) {
            textViewMentorName.setText(getResources().getString(R.string.help_no_mentor_label));
        } else {
            textViewMentorName.setText(getResources().getString(R.string.your_mentor_is) + " " + mentor.getFirstName());
        }
    }

    private void setDate (TextView view){

        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");//formatting for date
        String date = formatter.format(today);
        view.setText("Next Catchup : \n" + date);
    }


}