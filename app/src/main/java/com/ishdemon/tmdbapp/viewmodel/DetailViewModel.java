package com.ishdemon.tmdbapp.viewmodel;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ishdemon.tmdbapp.BR;
import com.ishdemon.tmdbapp.GlideApp;
import com.ishdemon.tmdbapp.MyApp;
import com.ishdemon.tmdbapp.data.MoviesApi;
import com.ishdemon.tmdbapp.model.DetailsResponse;
import com.ishdemon.tmdbapp.utils.DateParser;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DetailViewModel extends BaseObservable {
    private ObservableField<String> mUrl;
    private int id;
    public ObservableField<String> mDesc;
    public ObservableField<String> mRuntime;
    public ObservableField<String> mDate;
    public ObservableField<String> mGenre;
    public ObservableField<String> mBudget;
    public ObservableField<String> mRevenue;
    public ObservableField<String> mRating;
    public ObservableField<String> mLang;
    private MoviesApi moviesApi;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DetailViewModel(Integer id, @NonNull Context context) {
        this.id = id;
        this.context = context;
        this.mUrl = new ObservableField<>();
        this.mDesc = new ObservableField<>();
        this.mRuntime = new ObservableField<>();
        this.mDate = new ObservableField<>();
        this.mGenre = new ObservableField<>();
        this.mBudget = new ObservableField<>();
        this.mRevenue = new ObservableField<>();
        this.mRating = new ObservableField<>();
        this.mLang = new ObservableField<>();
        this.moviesApi = MyApp.create(context).getMoviesApi();
        fetchMovieDetails(id);
    }

    private void fetchMovieDetails(final int movieID) {
        MyApp myApp = MyApp.create(context);
        Disposable disposable = moviesApi.getMovieDetails(movieID, MyApp.getApikey())
                .subscribeOn(myApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DetailsResponse>() {
                               @Override
                               public void accept(DetailsResponse response) {
                                   updateDetails(response);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) {
                                   //Log.wtf("data:refresh" + page, "failed" + throwable.getCause());

                               }
                           }

                );
        compositeDisposable.add(disposable);
    }

    public static String coolNumberFormat(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        DecimalFormat format = new DecimalFormat("0.#");
        String value = format.format(count / Math.pow(1000, exp));
        return String.format("%s%c", value, "KMBTPE".charAt(exp - 1));
    }

    private void updateDetails(DetailsResponse response) {
        this.mDesc.set(response.getOverview());
        this.mRuntime.set(String.valueOf(response.getRuntime()) + " minutes");
        this.mDate.set(DateParser.getFormattedDate(response.getReleaseDate()));
        String genres = "";
        for (int i = 0; i < response.getGenres().size(); i++) {
            genres = genres + response.getGenres().get(i).getName() + ",";
        }
        this.mGenre.set(genres);
        this.mBudget.set(coolNumberFormat(response.getBudget()));
        this.mRevenue.set(coolNumberFormat(response.getRevenue()));
        this.mRating.set(response.getVoteAverage().toString());
        this.mLang.set(response.getOriginalLanguage().toUpperCase());
        this.mUrl.set("https://image.tmdb.org/t/p/w500" + response.getBackdropPath());
        notifyPropertyChanged(BR.preview);
        notifyChange();
    }

    @Bindable
    public String getPreview() {
        return mUrl.get();
    }

    @BindingAdapter("pictureUrl")
    public static void setPictureUrl(final ImageView imageView, String url) {
        GlideApp.with(imageView.getContext()).load(url).priority(Priority.HIGH).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

}
