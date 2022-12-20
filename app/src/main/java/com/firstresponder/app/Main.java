package com.firstresponder.app;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class Main extends Application {

    @Override
    public void onCreate() {
        DynamicColors.applyToActivitiesIfAvailable(this);
        super.onCreate();
    }
}
