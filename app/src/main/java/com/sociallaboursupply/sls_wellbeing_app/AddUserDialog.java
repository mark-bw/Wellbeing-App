package com.sociallaboursupply.sls_wellbeing_app;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.sociallaboursupply.sls_wellbeing_app.Database.UserDatabaseHandler;
import com.sociallaboursupply.sls_wellbeing_app.Model.UserModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

// Based off of: https://github.com/adrianseraspi12/Android-Tutorials/tree/master/Fullscreen%20Dialog
public class AddUserDialog extends DialogFragment implements View.OnClickListener {

    private Callback callback;
    private boolean isEditing = false;
    private View dialog;
    private List<UserModel> mentors;
    private Integer selectedMentorId;
    private UserModel selectedUser;

    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText phoneInput;
    private SwitchMaterial mentorSwitch;


    static AddUserDialog newInstance() {
        return new AddUserDialog();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = inflater.inflate(R.layout.add_user_dialog, container, false);

        firstNameInput = (EditText) dialog.findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) dialog.findViewById(R.id.lastNameInput);
        emailInput = (EditText) dialog.findViewById(R.id.emailInput);
        passwordInput = (EditText) dialog.findViewById(R.id.passwordInput);
        phoneInput = (EditText) dialog.findViewById(R.id.phoneInput);
        mentorSwitch = (SwitchMaterial) dialog.findViewById(R.id.mentorCheckbox);

        ImageButton close = dialog.findViewById(R.id.fullscreen_dialog_close);
        TextView action = dialog.findViewById(R.id.fullscreen_dialog_action);
        TextView title = dialog.findViewById(R.id.fullscreen_dialog_title);
        ConstraintLayout editMentorView = dialog.findViewById(R.id.editMentorView);

        close.setOnClickListener(v -> closeDialog());
        action.setOnClickListener(v -> save());
        editMentorView.setOnClickListener(v -> openMentorSelector());

        UserDatabaseHandler userDBh = new UserDatabaseHandler(dialog.getContext());

        if (selectedUser != null) {
            isEditing = true;
            mentors = userDBh.getAllOtherMentors(selectedUser.getId());
            title.setText("Edit User");
            setInitialUserData(selectedUser);
        } else {
            mentors = userDBh.getAllMentors();
        }

        return dialog;
    }

    private void closeDialog() {
        dismiss();
    }

    public void setUser(UserModel user) {
        selectedUser = user;
    }

    private void setInitialUserData(UserModel user) {
        firstNameInput.setText(user.getFirstName());
        lastNameInput.setText(user.getLastName());
        emailInput.setText(user.getEmail());
        emailInput.setEnabled(false);
        passwordInput.setText(user.getPassword());
        phoneInput.setText(user.getPhone());
        mentorSwitch.setChecked(user.getMentor());
        selectedMentorId = user.getMentorId();
        setMentorName(selectedMentorId);
    }

    private void save() {
        UserModel user = selectedUser;
        if (selectedUser == null) {
            user = new UserModel();
        }
        user.setFirstName(firstNameInput.getText().toString());
        user.setLastName(lastNameInput.getText().toString());
        user.setEmail(emailInput.getText().toString());
        user.setPassword(passwordInput.getText().toString());
        user.setPhone(phoneInput.getText().toString());
        user.setMentor(mentorSwitch.isChecked());
        user.setMentorId(selectedMentorId);
        callback.onActionClick(user);
    }

    @Override
    public void onClick(View v) {
    }

    public interface Callback {
        void onActionClick(UserModel user);
    }

    // Creates and opens the mentor selector dialog
    private void openMentorSelector() {
        Context context = dialog.getContext();

        LayoutInflater li = LayoutInflater.from(context);
        View selectMentorDialog = li.inflate(R.layout.select_mentor_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(selectMentorDialog);

        if (mentors.isEmpty()) {
            TextView noMentorText = selectMentorDialog.findViewById(R.id.noMentorText);
            noMentorText.setVisibility(View.VISIBLE);
        }

        // Each radio button has it's ID = to the mentor's user ID so it's easier to find
        RadioGroup radioGroup = selectMentorDialog.findViewById(R.id.mentorList);
        for (UserModel mentor : mentors) {
            RadioButton rb = generateRadioButton(context, mentor);
            radioGroup.addView(rb);
        }
        if (selectedMentorId != null) {
            radioGroup.check(selectedMentorId);
        }

        // Create the alert and configure the OK and Clear buttons
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, id) -> {
                            // Get the selected button value
                            selectedMentorId = radioGroup.getCheckedRadioButtonId();
                            setMentorName(selectedMentorId);
                        })
                .setNegativeButton("Clear",
                        (dialog, id) -> {
                            selectedMentorId = null;
                            setMentorName(selectedMentorId);
                        });
        // Create and open the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Displays the selected mentor's name in the set mentor component of the user edit dialog
    private void setMentorName(Integer selectedMentorId) {
        String userName = "";
        System.out.println(selectedMentorId);
        // null if the Clear button is used
        // -1 if the user made no selection but selected OK
        // 0 if there is no default value in the DB
        if (selectedMentorId == null || selectedMentorId < 1) {
            userName = "No mentor";
        } else {
            for (UserModel mentor : mentors) {
                if (mentor.id == selectedMentorId) {
                    userName = mentor.getFirstName() + " " + mentor.getLastName();
                }
            }
        }
        TextView mentorName = dialog.findViewById(R.id.mentorNameText);
        mentorName.setText(userName);
    }

    // Programmatically creating and styling the radio buttons
    // Each radio button has it's ID = to the mentor's user ID so it's easier to find
    private RadioButton generateRadioButton(Context context, UserModel mentor) {
        RadioButton rb = new RadioButton(context);
        rb.setText(mentor.getFirstName() + " " + mentor.getLastName());
        rb.setId(mentor.getId());
        int padding16dp = dpToPx(16, context);
        rb.setPadding(padding16dp, padding16dp, padding16dp, padding16dp);
        return rb;
    }

    // Helper function to convert dp units into pixels
    // via https://stackoverflow.com/a/29665208
    private int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
