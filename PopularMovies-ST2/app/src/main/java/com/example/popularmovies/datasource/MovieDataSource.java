package com.example.popularmovies.datasource;

import android.util.Log;

import com.example.popularmovies.api.MoviesDbRepository;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.ResultMovies;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {
    public static final int PAGE_SIZE = 20;
    private static final int FIRST_PAGE = 1;

    private String mSortMode="top_rated";

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        Log.i("Observe",mSortMode);
        MoviesDbRepository.getInstance().getMoviesByPageCb(new Callback<ResultMovies>() {
            @Override
            public void onResponse(Call<ResultMovies> call, Response<ResultMovies> response) {

                if(response != null && response.body()!=null){
                    List<Movie> lm = response.body().getMovies();
                    callback.onResult(lm,null, FIRST_PAGE+1);
                }
            }
            @Override
            public void onFailure(Call<ResultMovies> call, Throwable t) {

            }
        },mSortMode, FIRST_PAGE);

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;

        MoviesDbRepository.getInstance().getMoviesByPageCb(new Callback<ResultMovies>() {
            @Override
            public void onResponse(Call<ResultMovies> call, Response<ResultMovies> response) {
                if(response != null && response.body()!=null){
                    callback.onResult(response.body().getMovies(),adjacentKey);
                }
            }
            @Override
            public void onFailure(Call<ResultMovies> call, Throwable t) {

            }
        },mSortMode, params.key);
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {

        MoviesDbRepository.getInstance().getMoviesByPageCb(new Callback<ResultMovies>() {
            @Override
            public void onResponse(Call<ResultMovies> call, Response<ResultMovies> response) {
                if(response != null && response.body()!=null){
                    List<Movie> lm = response.body().getMovies();
                    Integer key =  lm.size()>0? params.key + 1 : null;
                    callback.onResult(response.body().getMovies(),key);
                }
            }
            @Override
            public void onFailure(Call<ResultMovies> call, Throwable t) {

            }
        },mSortMode, params.key);
    }

    public void setSortMode(String sortMode){
        mSortMode = sortMode;
    }

}
