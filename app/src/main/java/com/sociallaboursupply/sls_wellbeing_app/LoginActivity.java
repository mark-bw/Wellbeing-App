package com.sociallaboursupply.sls_wellbeing_app;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.sociallaboursupply.sls_wellbeing_app.Database.UserDatabaseHandler;
import com.sociallaboursupply.sls_wellbeing_app.Model.UserModel;
import com.sociallaboursupply.sls_wellbeing_app.Utils.DummyData;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button buttonLogin;
    MaterialButton passwordVisibilityButton;
    ColorStateList visibilityButtonColors;

    EditText editTextEmail;
    EditText editTextPassword;

    UserDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(PreferenceData.getUserLoggedInStatus(this)){
            if (PreferenceData.userIsAdmin(this)) {
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            } else if (PreferenceData.userIsMentor(this)) {
                startActivity(new Intent(LoginActivity.this, MainMentorActivity.class));
            } else {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            finish();
        }
        setContentView(R.layout.activity_login);

        // Get page views
        buttonLogin = findViewById(R.id.buttonLogin);
        passwordVisibilityButton = findViewById(R.id.passwordVisibilityButton);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        // insert dummy user into database if database is empty
        db = new UserDatabaseHandler(this);
        if(db.getAllAccounts().isEmpty()) {
            db.insert(DummyData.createDummyMentor());
            // IMPORTANT: dummy user now relies on there being a dummy mentor with ID 1 in the DB first
            db.insert(DummyData.createDummyUser());
        }

        // Login button directs user to main page or admin page based on user logging in
        buttonLogin.setOnClickListener(v -> {

            // lookup email and password in database for a match
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            List<UserModel> matchingUsers;
            matchingUsers = db.getUserWithEmailAndPassword(email, password);

            // if there is a match, log the user in and save user preferences
            if(!matchingUsers.isEmpty()) {
                UserModel user = matchingUsers.get(0);
                if(user.isMentor){
                    PreferenceData.setUserType(LoginActivity.this, PreferenceData.USER_TYPE_MENTOR);
                    startActivity(new Intent(LoginActivity.this, MainMentorActivity.class));
                } else {
                    PreferenceData.setUserType(LoginActivity.this, PreferenceData.USER_TYPE_USER);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                PreferenceData.setUserID(LoginActivity.this, user.id);
                PreferenceData.setUserLoggedInStatus(LoginActivity.this, true);
                overridePendingTransition(R.anim.scale_fade_in, 0);
                finish();
            } else if(email.equals("admin") && password.equals("admin")) { // hardcode admin login
                PreferenceData.setUserLoggedInStatus(LoginActivity.this, true);
                PreferenceData.setUserType(LoginActivity.this, PreferenceData.USER_TYPE_ADMIN);
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                overridePendingTransition(R.anim.scale_fade_in, 0);
                finish();
            } else {
                // display message if credentials are incorrect
                Snackbar.make(findViewById(R.id.loginView),
                        R.string.error_incorrect_login, Snackbar.LENGTH_SHORT).show();
            }
        });
        passwordVisibilityButton.setOnClickListener(v -> togglePasswordVisibility());
        visibilityButtonColors = passwordVisibilityButton.getIconTint();
        passwordVisibilityButton.setIconTintResource(R.color.medGrey);
    }

    private void togglePasswordVisibility() {
        int cursorPosition = editTextPassword.getSelectionStart();
        if (editTextPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordVisibilityButton.setIconTint(visibilityButtonColors);
        } else {
            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordVisibilityButton.setIconTintResource(R.color.medGrey);
        }
        editTextPassword.setSelection(cursorPosition);
    }
}