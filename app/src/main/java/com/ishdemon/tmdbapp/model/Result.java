package com.ishdemon.tmdbapp.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ishdemon.tmdbapp.GlideApp;
import com.ishdemon.tmdbapp.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import androidx.annotation.NonNull;

public class Result extends AbstractItem<Result, Result.ViewHolder> {

    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @Override
    public int getType() {
        return R.id.item_parent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_movie_list;
    }

    @Override
    public Result.ViewHolder getViewHolder(@NonNull View v) {
        return new Result.ViewHolder(v);
    }


    protected static class ViewHolder extends FastAdapter.ViewHolder<Result> {
        private ImageView posterImage;
        private TextView title;
        private TextView desc;
        private TextView rating;
        private TextView date;
        private TextView language;

        public ViewHolder(View view) {
            super(view);
            this.posterImage = view.findViewById(R.id.img_poster);
            this.title = view.findViewById(R.id.tv_title);
            this.desc = view.findViewById(R.id.tv_desc);
            this.rating = view.findViewById(R.id.tv_rating);
            this.date = view.findViewById(R.id.tv_date);
            this.language = view.findViewById(R.id.tv_lang);
        }

        @Override
        public void bindView(Result item, List<Object> payloads) {
            String url = "https://image.tmdb.org/t/p/w185";
            GlideApp.with(posterImage.getContext()).load(url + item.posterPath).transition(DrawableTransitionOptions.withCrossFade()).into(posterImage);
            title.setText(item.title);
            desc.setText(item.overview);
            rating.setText(String.valueOf(item.voteAverage));
            date.setText(item.releaseDate);
            language.setText(item.originalLanguage.toUpperCase());
        }

        @Override
        public void unbindView(Result item) {

        }
    }


}
