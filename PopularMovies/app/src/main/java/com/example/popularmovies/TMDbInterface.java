package com.example.popularmovies;

import com.example.popularmovies.model.Detail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbInterface {
    @GET("movie/{sort}")
    Call<ResponseModel> getMovies(@Path("sort") String sort,
                                        @Query("api_key") String apiKey,
                                         @Query("language") String language,
                                         @Query("page") int page
    );
    @GET("movie/{movie_id}")
    Call<Detail> getDetail(@Path("movie_id") String id,
                           @Query("api_key") String apiKey,
                           @Query("language") String language,
                           @Query("append_to_response") String appendToResponse
    );
}
