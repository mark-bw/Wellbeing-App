package com.sociallaboursupply.sls_wellbeing_app.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sociallaboursupply.sls_wellbeing_app.Model.TaskModel;
import com.sociallaboursupply.sls_wellbeing_app.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MentorTaskAdapter extends RecyclerView.Adapter<MentorTaskAdapter.ViewHolder> {
    private Callback callback;
    private List<TaskModel> taskList;
    private final Activity activity;

    public MentorTaskAdapter(Activity activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mentor_task_item, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        // Get the task at current position
        final TaskModel item = taskList.get(position);

        // if the task is marked as completed, strike out the text
        if(item.getStatus() != 0){
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.description.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // Set the text and checkbox of the task
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());

        // Add a listener to the checkbox, if a box is checked the database status will be updated
        holder.editButton.setOnClickListener(v -> {
            callback.onEditTask(item);
        });
        holder.deleteButton.setOnClickListener(v -> {
            callback.onDeleteTask(item);
        });
    }

    public void setCallback(MentorTaskAdapter.Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onDeleteTask(TaskModel task);
        void onEditTask(TaskModel task);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<TaskModel> taskList){
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public int getItemCount(){
        return taskList.size();
    }

    public Context getContext(){
        return activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        ImageView editButton;
        ImageView deleteButton;

        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.titleTextView);
            description = view.findViewById(R.id.descriptionTextView);
            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);
        }
    }
}
