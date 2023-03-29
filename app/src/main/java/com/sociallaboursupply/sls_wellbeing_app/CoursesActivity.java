package com.sociallaboursupply.sls_wellbeing_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.sociallaboursupply.sls_wellbeing_app.Adapter.CourseAdapter;
import com.sociallaboursupply.sls_wellbeing_app.Model.CourseModel;
import com.sociallaboursupply.sls_wellbeing_app.Utils.DummyData;
import com.sociallaboursupply.sls_wellbeing_app.Utils.SubActivity;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CoursesActivity extends SubActivity {

    String api_key = "AIzaSyCyouSNhzdyM_5ln4b5m378NGRARgZfZkE";
    private YouTubePlayer youTubePlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        setTitle(R.string.courses_page_header);

        // Initialise Youtube Player
        YouTubePlayerFragment youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
                .findFragmentById(R.id.youtubePlayerViewVideoOfTheDay);

        youTubePlayerFragment.initialize(api_key, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;

                    //set the player style default
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //cue the video to be played
                    youTubePlayer.cueVideo("2Mm3-rjJmf0");
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                //print or show error if initialization failed
                Log.e(CoursesActivity.class.getSimpleName(), "Youtube Player View initialization failed");
            }
        });

        String db = "Replace String for DatabaseHandler";
//        db.openDatabase();

        // Set RecyclerView for courses to be a linearlayout
        RecyclerView coursesRecyclerView = findViewById(R.id.recyclerViewCoursesList);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        coursesRecyclerView.setNestedScrollingEnabled(false);

        // Create and bind adapter to recyclerview
        CourseAdapter courseAdapter = new CourseAdapter(db, CoursesActivity.this);
        coursesRecyclerView.setAdapter(courseAdapter);

        // Create Dummy Courses
        List<CourseModel> courseList = DummyData.createDummyCourseData();

        // If there are no courses display a message
        View emptyView = findViewById(R.id.textViewEmptyCoursesList);
        if(courseList.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }

        // Pass courses onto the adapter to be displayed
        courseAdapter.setCourses(courseList);
    }
}