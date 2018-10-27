package com.amit7itz.motivator.motivator;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

class Messages {
    public static void showMessage(Context context, String title, String msg) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setTitle(title);
        dlgAlert.setMessage(msg);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public static void showMessage(Context context, String msg) {
        showMessage(context, "Motivator", msg);
    }

    public static void showYesNoMessage(Context context, String title, String msg , final Runnable yes_callback, final Runnable no_callback) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setTitle(title);
        dlgAlert.setMessage(msg);
        dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
                if (yes_callback != null) {
                    yes_callback.run();
                }
            }
        });
        dlgAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
                if (no_callback != null) {
                    no_callback.run();
                }
            }
        });
        dlgAlert.create().show();
    }
}
