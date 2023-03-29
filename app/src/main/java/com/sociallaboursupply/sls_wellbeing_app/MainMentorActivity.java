package com.sociallaboursupply.sls_wellbeing_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.sociallaboursupply.sls_wellbeing_app.Adapter.UserAdapter;
import com.sociallaboursupply.sls_wellbeing_app.Database.UserDatabaseHandler;
import com.sociallaboursupply.sls_wellbeing_app.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainMentorActivity extends AppCompatActivity {

    private UserDatabaseHandler userDB;
    private List<UserModel> users;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mentor);
        setTitle("Mentor Home");

        userDB = new UserDatabaseHandler(this);
        this.users = userDB.getAllUsers();

        refreshUsers();
    }

    private void refreshUsers() {

        // Set RecyclerView for users to be a linearlayout
        RecyclerView userRecyclerView = findViewById(R.id.userRecyclerMentor);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and bind adapter to recyclerview
        UserAdapter userAdapter = new UserAdapter();
        userRecyclerView.setAdapter(userAdapter);

        // Get users from database
        List<UserModel> userList = new ArrayList<>(users);

        // Pass users onto the adapter to be displayed
        userAdapter.setUsers(userList);
        ((UserAdapter) userAdapter).setCallback(new UserAdapter.Callback() {
            @Override
            public void onUserClick(UserModel user) {
                // Passing parameters to next activity via https://stackoverflow.com/a/3913720
                Intent intent = new Intent(MainMentorActivity.this, MentorViewMenteeActivity.class);
                Bundle b = new Bundle();
                b.putInt("userid", user.getId()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });
    }

    // Menu log out option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout_menu) {
            PreferenceData.setUserLoggedInStatus(this, false);
            startActivity(new Intent(MainMentorActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.scale_fade_in, 0);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}