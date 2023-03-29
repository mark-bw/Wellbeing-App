package com.sociallaboursupply.sls_wellbeing_app.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sociallaboursupply.sls_wellbeing_app.Model.UserModel;
import com.sociallaboursupply.sls_wellbeing_app.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<UserModel> userList;
    private Callback callback;

    public UserAdapter(){

    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position){
        // Get the item at current position
        final UserModel item = userList.get(position);

        // Set the text of the user item
        holder.fullNameTextView.setText(item.getFirstName() + " " + item.getLastName());
        holder.emailTextView.setText(item.getEmail());
        if (item.getMentor()) {
            holder.mentorTextView.setVisibility(View.VISIBLE);
        } else {
            holder.mentorTextView.setVisibility(View.GONE);
        }

        holder.view.setOnClickListener(v -> {
            callback.onUserClick(item);
        });

    }
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onUserClick(UserModel user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUsers(List<UserModel> userList){
        this.userList = userList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameTextView;
        TextView emailTextView;
        TextView mentorTextView;
        View view;

        ViewHolder(View view){
            super(view);
            this.view = view;
            fullNameTextView = view.findViewById(R.id.fullNameTextView);
            emailTextView = view.findViewById(R.id.emailTextView);
            mentorTextView = view.findViewById(R.id.mentorTextView);
        }
    }

}
