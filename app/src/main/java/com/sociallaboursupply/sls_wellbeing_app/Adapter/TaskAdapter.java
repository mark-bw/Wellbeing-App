package com.sociallaboursupply.sls_wellbeing_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sociallaboursupply.sls_wellbeing_app.Model.TaskModel;
import com.sociallaboursupply.sls_wellbeing_app.R;
import com.sociallaboursupply.sls_wellbeing_app.TaskActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<TaskModel> taskList;
    private final TaskActivity activity;

    public TaskAdapter(TaskActivity activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        // Get the task at current position
        final TaskModel item = taskList.get(position);

        // if the task is marked as completed, strike out the text
        if(item.getStatus() != 0){
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.description.setPaintFlags(holder.description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // Set the text and checkbox of the task
        holder.title.setText(item.getTitle());
        holder.title.setChecked(item.getStatus() != 0);
        if (item.getDescription() != null && item.getDescription().length() < 1) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setText(item.getDescription());
            holder.description.setVisibility(View.VISIBLE);
        }

        // Add a listener to the checkbox, if a box is checked the database status will be updated
        holder.title.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
                holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); // Strike out the text
                holder.description.setPaintFlags(holder.description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); // Strike out the text
                activity.updateTaskStatus(item.getId(), 1);
            } else {
                holder.title.setPaintFlags(holder.title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG)); // Text back to default
                holder.description.setPaintFlags(holder.description.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG)); // Text back to default
                activity.updateTaskStatus(item.getId(), 0);
            }
        });
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
        CheckBox title;
        TextView description;

        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.tasksCheckbox);
            description = view.findViewById(R.id.taskDescTextView);
        }
    }
}
