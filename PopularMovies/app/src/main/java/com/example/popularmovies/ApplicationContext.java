package com.example.popularmovies;

import android.app.Application;
import android.content.res.Resources;

public class ApplicationContext extends Application {
    protected static ApplicationContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Resources getResourcesStatic() {
        return instance.getResources();
    }
}
