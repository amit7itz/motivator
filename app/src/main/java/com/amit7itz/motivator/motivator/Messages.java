package com.amit7itz.motivator.motivator;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

class Messages {
    public static void showMessage(Context context, String msg) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setTitle("Motivator");
        dlgAlert.setMessage(msg);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}
