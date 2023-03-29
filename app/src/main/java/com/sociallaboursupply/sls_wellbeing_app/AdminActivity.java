package com.sociallaboursupply.sls_wellbeing_app;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sociallaboursupply.sls_wellbeing_app.Adapter.UserAdapter;
import com.sociallaboursupply.sls_wellbeing_app.Database.UserDatabaseHandler;
import com.sociallaboursupply.sls_wellbeing_app.Model.UserModel;
import com.sociallaboursupply.sls_wellbeing_app.Utils.Popup;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminActivity extends AppCompatActivity {

    ExtendedFloatingActionButton createUserButton;
    UserDatabaseHandler userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setTitle("Admin User Management");

        createUserButton = findViewById(R.id.addUserButton);
        createUserButton.setOnClickListener(v -> createUser());

        userDB = new UserDatabaseHandler(this);

        refreshUsers();
    }

    private void refreshUsers() {
        List<UserModel> users = userDB.getAllAccounts();
        // TODO: sort by first name;

        // Set RecyclerView for users to be a linearlayout
        RecyclerView userRecyclerView = findViewById(R.id.userRecycler);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and bind adapter to recyclerview
        UserAdapter userAdapter = new UserAdapter();
        userRecyclerView.setAdapter(userAdapter);

        // Get users from database
        List<UserModel> userList = new ArrayList<>(users);

//<<<<<<< feature-124-add-mentor-account
        // Pass users onto the adapter to be displayed
//=======
        // Adds a click event listener to each user component
//>>>>>>> master
        userAdapter.setUsers(userList);
        ((UserAdapter) userAdapter).setCallback(new UserAdapter.Callback() {
            @Override
            public void onUserClick(UserModel user) {
                editUser(user);
            }
        });
    }

    private void createUser() {
        openUserEditor(null);
    }
    private void editUser(UserModel user) {
        openUserEditor(user);
    }
    private void openUserEditor(UserModel user) {
        AddUserDialog dialog = AddUserDialog.newInstance();
        dialog.setUser(user);
        ((AddUserDialog) dialog).setCallback(new AddUserDialog.Callback() {
            @Override
            public void onActionClick(UserModel user) {
                try {
                    if (user.getId() == null) {
                        userDB.insert(user);
                    } else {
                        userDB.update(user);
                    }
                    dialog.dismiss();
                    refreshUsers();
                    Snackbar.make(findViewById(R.id.adminActivity),
                            "Saved " + user.getFirstName() + " " + user.getLastName(), Snackbar.LENGTH_SHORT).show();
                } catch (SQLiteConstraintException error) {
                    // Error gets thrown when email address is already used due to a UNIQUE constraint property
                    Popup.alert(AdminActivity.this, "Error", "Email address already in use.  Please use a different one.");
                }
            }
        });
        dialog.show(getSupportFragmentManager(), "edit_user_dialog");
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
            PreferenceData.clearPreferences(this);
            startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.scale_fade_in, 0);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}