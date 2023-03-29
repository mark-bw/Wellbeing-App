package com.sociallaboursupply.sls_wellbeing_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.sociallaboursupply.sls_wellbeing_app.Adapter.MoodAdapter;
import com.sociallaboursupply.sls_wellbeing_app.Database.MoodDatabaseHandler;
import com.sociallaboursupply.sls_wellbeing_app.Model.MoodModel;
import com.sociallaboursupply.sls_wellbeing_app.Utils.DummyData;
import com.sociallaboursupply.sls_wellbeing_app.Utils.MoodChart;
import com.sociallaboursupply.sls_wellbeing_app.Utils.SubActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MoodTrackerActivity extends SubActivity {

    LinearLayout buttonContainer;
    Button buttonMoodRatingSad;
    Button buttonMoodRatingNeutral;
    Button buttonMoodRatingHappy;

    MoodDatabaseHandler moodDB;
    List<MoodModel> moods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracker);
        setTitle(R.string.mood_tracker_page_heading);

        // NOTE: it may not be ideal to start the database in this thread
        // Creates table in the database for tasks if one does not exists, and opens database
        moodDB = new MoodDatabaseHandler(this);

        // insert dummy mood data into database if database is empty
        if(moodDB.getUserMoods(PreferenceData.getUserID(this)).isEmpty()) {
            for(MoodModel mood : DummyData.createDummyMoodData(PreferenceData.getUserID(this))){
                moodDB.insert(mood);
            }
        }

        // NOTE: only update the displayed content after the DB is available (openDatabase)
        updateContent();

        buttonContainer = findViewById(R.id.moodButtonContainer);
        buttonMoodRatingSad = findViewById(R.id.imageButtonSadFace);
        buttonMoodRatingNeutral = findViewById(R.id.imageButtonNeutralFace);
        buttonMoodRatingHappy = findViewById(R.id.imageButtonHappyFace);

        buttonMoodRatingSad.setOnClickListener(view -> createMood(MoodModel.SAD));
        buttonMoodRatingNeutral.setOnClickListener(view -> createMood(MoodModel.NEUTRAL));
        buttonMoodRatingHappy.setOnClickListener(view -> createMood(MoodModel.HAPPY));
    }

    // Opens the Mood dialog for a new entry
    private void createMood(String status) {
        MoodModel mood = new MoodModel();
        mood.setStatus(status);
        openPrompt(mood);
    }
    // Saves rating and note to the DB
    private void createMoodEntry(String rating, String note) {
        // Hide the mood prompt card
        View moodEntryCard = findViewById(R.id.moodEntryCard);
        moodEntryCard.setVisibility(View.GONE);

        // Modern toast alert
        Snackbar.make(findViewById(R.id.moodTrackerView),
                getText(R.string.general_saved), Snackbar.LENGTH_SHORT).show();

        // Insert mood in DB
        MoodModel mood = new MoodModel();
        mood.setUserId(PreferenceData.getUserID(this));
        mood.setStatus(rating);
        mood.setDate(new Date());
        mood.setNote(note);
        moodDB.insert(mood);

        updateContent();
    }

    // Edit an existing mood.  Called by MoodAdapter
    public void editMood(MoodModel mood) {
        openPrompt(mood);
    }
    // Update mood entry in DB
    private void updateMoodEntry(MoodModel oldMood, String rating, String userNotes) {
        moodDB.updateMoodDetails(oldMood.getId(), rating, userNotes);
        updateContent();
    }

    // Initialises and then opens the mood creator dialog
    private void openPrompt(MoodModel mood) {
        boolean isEditing = mood.getId() != null;

        String selectedRating = mood.getStatus();
        String selectedNote = mood.getNote();

        // Used to create the alert dialog box
        LayoutInflater li = LayoutInflater.from(this);
        // promptsView uses the mood_dialog layout
        View promptsView = li.inflate(R.layout.mood_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        if (isEditing) {
            TextView dialogTitle = promptsView.findViewById(R.id.textViewMoodTrackerPrompt);
            dialogTitle.setText(getText(R.string.mood_tracker_edit_mood_title));
        }

        // Set the default text for the mood notes
        EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setText(selectedNote);

        // Select the button the user selected
        int selectedButtonId;
        switch (selectedRating) {
            case MoodModel.SAD:
                selectedButtonId = R.id.dialogButtonSadFace;
                break;
            case MoodModel.NEUTRAL:
                selectedButtonId = R.id.dialogButtonNeutralFace;
                break;
            case MoodModel.HAPPY:
                selectedButtonId = R.id.dialogButtonHappyFace;
                break;
            default:
                selectedButtonId = R.id.dialogButtonNeutralFace;
        }
        MaterialButtonToggleGroup toggleGroup = (MaterialButtonToggleGroup) promptsView.findViewById(R.id.moodToggleGroup);
        toggleGroup.check(selectedButtonId);

        // Create the alert and configure the save and cancel buttons
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getText(R.string.general_save),
                        (dialog, id) -> {
                            // Get the selected button value
                            int selectedId = toggleGroup.getCheckedButtonId();
                            if (selectedId != View.NO_ID) {
                                String rating;
                                switch (selectedId) {
                                    case R.id.dialogButtonSadFace:
                                        rating = MoodModel.SAD;
                                        break;
                                    case R.id.dialogButtonNeutralFace:
                                        rating = MoodModel.NEUTRAL;
                                        break;
                                    case R.id.dialogButtonHappyFace:
                                        rating = MoodModel.HAPPY;
                                        break;
                                    default:
                                        rating = MoodModel.NEUTRAL;
                                }
                                // Save the user response
                                String userNotes = userInput.getText().toString();
                                if (isEditing) {
                                    updateMoodEntry(mood, rating, userNotes);
                                } else {
                                    createMoodEntry(rating, userNotes);
                                }
                            }
                        })
                .setNegativeButton(getText(R.string.general_cancel),
                        (dialog, id) -> dialog.cancel());

        // Create and open the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    // Load the mood data and then update the chart and list of moods
    private void updateContent() {
        moods = moodDB.getUserMoods(PreferenceData.getUserID(this));
        redrawChart();
        reloadRecycler();
    }

    private void reloadRecycler() {
        // Set RecyclerView for tasks to be a linearlayout
        RecyclerView moodRecyclerView = findViewById(R.id.moodTrackerRecycler);
        moodRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and bind adapter to recyclerview
        MoodAdapter moodAdapter = new MoodAdapter(MoodTrackerActivity.this);
        moodRecyclerView.setAdapter(moodAdapter);

        // Get tasks from database
        List<MoodModel> moodList = new ArrayList<>(moods);

        // If there are no tasks, hide the card
        View moodListCard = findViewById(R.id.moodListCard);
        if(moodList.isEmpty()){
            moodListCard.setVisibility(View.GONE);
        } else {
            moodListCard.setVisibility(View.VISIBLE);
        }

        // Pass tasks onto the adapter to be displayed
        Collections.reverse(moodList);
        moodAdapter.setMoods(moodList);
    }

    private void redrawChart() {
        LineChart lineChart = findViewById(R.id.chartMoodTracker);
        MoodChart.drawChart(moods, lineChart, this);
    }
}