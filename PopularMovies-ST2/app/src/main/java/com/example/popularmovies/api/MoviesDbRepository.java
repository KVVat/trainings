package com.example.popularmovies.api;

import android.util.Log;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.constants.Constants;
import com.example.popularmovies.interfaces.TMDbInterface;
import com.example.popularmovies.model.Detail;
import com.example.popularmovies.model.ResultMovies;
import com.example.popularmovies.model.Trailers;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public class MoviesDbRepository {

    private TMDbInterface api;
    private MoviesDbRepository(TMDbInterface api) {
        this.api = api;
    }

    private static MoviesDbRepository repository;

    /**
     *
     * @return repository instance
     */
    public static MoviesDbRepository getInstance() {
        if (repository == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesDbRepository(retrofit.create(TMDbInterface.class));
        }
        return repository;
    }

    public TMDbInterface getApi(){
        return this.api;
    }

    /**
     * getDetail As MutableLiveData
     */
    public void getDetail(Callback<Detail> callback,String id){

        api.getDetail(id, BuildConfig.TMDbAPIKEY, Constants.LANGUAGE,"")
                .enqueue(callback);

        return;
    }


    public void getMoviesByPageCb(Callback<ResultMovies> callback,String sort, int page){
        api.getMovies(sort, BuildConfig.TMDbAPIKEY, Constants.LANGUAGE, page).enqueue(callback);
    }

    public void getTrailers(Callback<Trailers> callback, String id){
        api.getTrailers(id, BuildConfig.TMDbAPIKEY, Constants.LANGUAGE).enqueue(callback);
    }
}
