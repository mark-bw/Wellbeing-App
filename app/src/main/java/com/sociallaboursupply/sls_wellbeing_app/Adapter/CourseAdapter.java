package com.sociallaboursupply.sls_wellbeing_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sociallaboursupply.sls_wellbeing_app.CoursesActivity;
import com.sociallaboursupply.sls_wellbeing_app.Model.CourseModel;
import com.sociallaboursupply.sls_wellbeing_app.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<CourseModel> courseList;
    private final CoursesActivity activity;

    // Todo: implement database for course list - replace String with a Database handler for courses
    private final String db;
//    private final DatabaseHandler db;

    public CourseAdapter(String db, CoursesActivity activity){
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position){

//        db.openDatabase();

        // Get the task at current position
        final CourseModel course = courseList.get(position);

        // Set the text and checkbox of the task
        holder.title.setText(course.getTitle());
        holder.description.setText(course.getDescription());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCourses(List<CourseModel> courseList){
        this.courseList = courseList;
        notifyDataSetChanged();
    }

    public int getItemCount(){
        return courseList.size();
    }

    public Context getContext(){
        return activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;

        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.courseTitle);
            description = view.findViewById(R.id.courseDescription);
        }
    }
}
