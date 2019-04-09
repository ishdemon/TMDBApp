package com.ishdemon.tmdbapp.data;

import com.ishdemon.tmdbapp.model.DetailsResponse;
import com.ishdemon.tmdbapp.model.DiscoverResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {

    @GET("discover/movie")
    Single<DiscoverResponse> getTopMovies(@Query("api_key") String api_key,
                                          @Query("sort_by") String sort,
                                          @Query("page") Integer page);

    @GET("movie/{id}")
    Single<DetailsResponse> getMovieDetails(
            @Path("id") Integer id, @Query("api_key") String api_key);
}
