package com.sociallaboursupply.sls_wellbeing_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sociallaboursupply.sls_wellbeing_app.Model.MoodModel;
import com.sociallaboursupply.sls_wellbeing_app.MoodTrackerActivity;
import com.sociallaboursupply.sls_wellbeing_app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.ViewHolder> {

    private List<MoodModel> moodList;
    private final MoodTrackerActivity activity;

    public MoodAdapter(MoodTrackerActivity activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mood_item, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        // Get the mood at current position
        final MoodModel item = moodList.get(position);

        // Set the note and date of the mood
        int icon = R.drawable.ic_neutral;
        String status = item.getStatus();
        if (status.contentEquals(MoodModel.SAD)) {
            icon = R.drawable.ic_sad;
        } else if (status.contentEquals(MoodModel.NEUTRAL)) {
            icon = R.drawable.ic_neutral;
        } else if (status.contentEquals(MoodModel.HAPPY)) {
            icon = R.drawable.ic_smile;
        } else {
            icon = R.drawable.ic_neutral;
        }
        holder.face.setImageResource(icon);

        Date date = item.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd E, h:mm a");
        String dateTime = dateFormat.format(date);
        holder.date.setText(dateTime);

        String note = item.getNote();
        if (note != null && note.length() > 0) {
            holder.note.setText(note);
        } else {
            holder.note.setVisibility(View.GONE);
        }

        // When a mood item is clicked, should open the edit dialog
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.editMood(item);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMoods(List<MoodModel> moodList){
        this.moodList = moodList;
        notifyDataSetChanged();
    }

    public int getItemCount(){
        return moodList.size();
    }

    public Context getContext(){
        return activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout card;
        ImageView face;
        TextView note;
        TextView date;

        ViewHolder(View view){
            super(view);
            card = view.findViewById(R.id.card);
            face = view.findViewById(R.id.moodFaceImage);
            note = view.findViewById(R.id.noteText);
            date = view.findViewById(R.id.dateText);
        }
    }
}
