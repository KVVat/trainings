package com.example.popularmovies.applilcation;

import android.app.Application;

public class ApplicationContext extends Application {
    public static ApplicationContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
