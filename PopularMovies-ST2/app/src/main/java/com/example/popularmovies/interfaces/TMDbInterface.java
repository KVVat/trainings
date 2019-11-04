package com.example.popularmovies.interfaces;

import com.example.popularmovies.model.Detail;
import com.example.popularmovies.model.ResultMovies;
import com.example.popularmovies.model.Reviews;
import com.example.popularmovies.model.Trailers;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbInterface {
    @GET("movie/{sort}")
    Call<ResultMovies> getMovies(@Path("sort") String sort,
                                 @Query("api_key") String apiKey,
                                 @Query("language") String language,
                                 @Query("page") int page
    );
    @GET("movie/{movie_id}")
    Observable<Detail> getDetail(@Path("movie_id") String id,
                                 @Query("api_key") String apiKey,
                                 @Query("language") String language,
                                 @Query("append_to_response") String appendToResponse
    );
    @GET("movie/{movie_id}/videos")
    Observable<Trailers> getTrailers(@Path("movie_id") String id,
                               @Query("api_key") String apiKey,
                               @Query("append_to_response") String appendToResponse
    );
    @GET("movie/{movie_id}/reviews")
    Observable<Reviews> getReviews(@Path("movie_id") String id,
                                    @Query("api_key") String apiKey,
                                    @Query("language") String language,
                                    @Query("page") int page
    );
}
