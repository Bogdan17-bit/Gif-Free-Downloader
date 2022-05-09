package com.example.giffreedownloader.error;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;

import com.example.giffreedownloader.MainActivity;
import com.example.giffreedownloader.systems.controller.NetworkProperty;

public class ErrorHandler {

    public static void controlConnection(Activity activity) {
        if(!NetworkProperty.hasConnection(activity.getApplicationContext())) {
            activity.finish();
            makeToast(activity, "No internet connection");
        }
    }

    public static void makeToast(Activity activity, String text) {
            Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
    }
}
