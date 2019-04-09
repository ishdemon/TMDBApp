package com.ishdemon.tmdbapp.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.ishdemon.tmdbapp.MyApp;
import com.ishdemon.tmdbapp.data.MoviesApi;
import com.ishdemon.tmdbapp.model.DiscoverResponse;
import com.ishdemon.tmdbapp.model.Result;

import java.util.List;
import java.util.Observable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainViewModel extends Observable {

    private MoviesApi moviesApi;
    public ObservableInt progress_spinner;
    public ObservableInt recyclerView;
    private Context context;
    private List<Result> movies;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainViewModel(@NonNull Context context) {
        this.context = context;
        this.progress_spinner = new ObservableInt(View.GONE);
        this.recyclerView = new ObservableInt(View.GONE);
        this.moviesApi = MyApp.create(context).getMoviesApi();
    }

    public void initializeViews() {
        progress_spinner.set(View.VISIBLE);
        fetchMovies(1);
    }

    public void scrollLoad(int page) {
        fetchMovies(page);
    }


    private void fetchMovies(final int page) {
        MyApp myApp = MyApp.create(context);
        Disposable disposable = moviesApi.getTopMovies(MyApp.getApikey(), "popularity.desc", page)
                .subscribeOn(myApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DiscoverResponse>() {
                               @Override
                               public void accept(DiscoverResponse response) {
                                   Log.wtf("data:refreshNEW" + page, "success" + response.getResults().size());
                                   if ((page == 1)) {
                                       changeDataSet(response.getResults(), true);
                                   } else {
                                       changeDataSet(response.getResults(), false);
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) {
                                   Log.wtf("data:refresh" + page, "failed" + throwable.getCause());

                               }
                           }

                );
        compositeDisposable.add(disposable);
    }

    private void changeDataSet(List<Result> movielist, Boolean argument) {
        if (argument) {
            progress_spinner.set(View.GONE);
            recyclerView.set(View.VISIBLE);
        }
        movies = movielist;
        setChanged();
        notifyObservers(argument);
    }

    public List<Result> getMovies() {
        return movies;
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }


}


