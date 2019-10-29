package com.example.popularmovies;

import android.util.Log;

import com.example.popularmovies.model.Detail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";

    private static MoviesRepository repository;
    public TMDbInterface api;
    private MoviesRepository(TMDbInterface api) {
        this.api = api;
    }
    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(TMDbInterface.class));
        }
        return repository;
    }

    public void getDetail(final TMDbGetDetailCallback callback,String id){
        Log.i("TEST","Run Get Detail"+id);
        api.getDetail(id,BuildConfig.IMDbAPIKEY, "en-US","")
                .enqueue(new Callback<Detail>() {
                    @Override
                    public void onResponse(Call<Detail> call, Response<Detail> response) {
                        Log.i("Test",response.toString());
                        if (response.isSuccessful()) {
                            Detail moviesResponse = response.body();
                            if (moviesResponse != null && moviesResponse.getId() != null) {
                                callback.onSuccess(moviesResponse);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }
                    @Override
                    public void onFailure(Call<Detail> call, Throwable t) {
                        Log.i("Test",t.getMessage()+","+t.getLocalizedMessage());
                        callback.onError();
                    }
                });
    }

    public void getMovies(String sort) {
        if(sort == null) sort = "top_rated";
        api.getMovies(sort,BuildConfig.IMDbAPIKEY, LANGUAGE, 1)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful()) {
                            ResponseModel moviesResponse = response.body();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                       Log.e("test","failure");
                    }
                });
    }
}
