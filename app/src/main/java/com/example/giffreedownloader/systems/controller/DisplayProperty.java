package com.example.giffreedownloader.systems.controller;

import android.app.Activity;
import android.graphics.Point;
import android.util.Pair;

public class DisplayProperty {

    static public Pair<Integer, Integer> getDisplaySize(Activity activity) {
        android.view.Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return new Pair<>(size.x, size.y);
    }

}
