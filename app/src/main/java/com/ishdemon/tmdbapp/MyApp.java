package com.ishdemon.tmdbapp;

import androidx.multidex.MultiDexApplication;

public class MyApp extends MultiDexApplication {

    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
