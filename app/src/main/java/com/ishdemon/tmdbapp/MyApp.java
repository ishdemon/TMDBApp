package com.ishdemon.tmdbapp;

import android.content.Context;
import android.util.Log;

import com.ishdemon.tmdbapp.data.MoviesApi;
import com.ishdemon.tmdbapp.data.MoviesFactory;

import androidx.multidex.MultiDexApplication;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class MyApp extends MultiDexApplication {
    private MoviesApi moviesApi;
    private Scheduler scheduler;
    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    private static MyApp get(Context context) {
        return (MyApp) context.getApplicationContext();
    }

    public static MyApp create(Context context) {
        return MyApp.get(context);
    }

    public static String getApikey() {
        return BuildConfig.API_KEY;
    }

    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }

    public MoviesApi getMoviesApi() {
        if (moviesApi == null) {
            moviesApi = MoviesFactory.create();
        }
        return moviesApi;
    }
}
