package com.sociallaboursupply.sls_wellbeing_app.Utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.sociallaboursupply.sls_wellbeing_app.R;

public class SocialIntents {

    public static void setupPhoneButton(Activity activity, View snackbarView, Button phoneButton, String phoneNumber) {
        if (phoneNumber == null || phoneNumber.contentEquals("")) {
            phoneButton.setVisibility(View.GONE);
        } else {
            phoneButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(intent);
                } else {
                    Snackbar.make(snackbarView,
                            R.string.error_feature_not_available, Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void setupSMSButton(Activity activity, View snackbarView, Button smsButton, String phoneNumber) {
        setupSMSButton(activity, snackbarView, smsButton, phoneNumber, "");
    }
    public static void setupSMSButton(Activity activity, View snackbarView, Button smsButton, String phoneNumber, String smsMessage) {
        if (phoneNumber == null || phoneNumber.contentEquals("")) {
            smsButton.setVisibility(View.GONE);
        } else {
            smsButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("sms:" + phoneNumber));
                intent.putExtra("sms_body", smsMessage);
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(intent);
                } else {
                    Snackbar.make(snackbarView,
                            R.string.error_feature_not_available, Snackbar.LENGTH_SHORT).show();
                }
            });

        }
    }

    public static void setupEmailButton(Activity activity, View snackbarView, Button emailButton, String emailAddress) {
        setupEmailButton(activity, snackbarView, emailButton, emailAddress, "", "");
    }
    public static void setupEmailButton(Activity activity, View snackbarView, Button emailButton, String emailAddress, String emailSubject, String emailMessage) {
        if (emailAddress == null || emailAddress.contentEquals("")) {
            emailButton.setVisibility(View.GONE);
        } else {
            emailButton.setOnClickListener(v -> {
                String[] emailAddresses = {emailAddress};
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, emailAddresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                intent.putExtra(Intent.EXTRA_TEXT, emailMessage);

                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                } else {
                    Snackbar.make(snackbarView,
                            R.string.error_feature_not_available, Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }
}
