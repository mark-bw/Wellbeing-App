package com.sociallaboursupply.sls_wellbeing_app.Utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class Popup {

    public static void alert(Context context, String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        alert.show();
    }
}
