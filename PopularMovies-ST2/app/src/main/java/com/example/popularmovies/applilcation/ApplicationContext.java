package com.example.popularmovies.applilcation;

import android.app.Application;
import android.content.res.Resources;

public class ApplicationContext extends Application {
    public static ApplicationContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
