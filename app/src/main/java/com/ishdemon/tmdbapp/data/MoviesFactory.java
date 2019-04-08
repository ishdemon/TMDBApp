package com.ishdemon.tmdbapp.data;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesFactory {
    private final static String BASE_URL = "https://api.themoviedb.org/3/";

    public static MoviesApi create() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(new OkHttpClient.Builder()
                .build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(MoviesApi.class);
    }
}
