package com.example.popularmovies.datasource;

import android.util.Log;

import com.example.popularmovies.api.FavoriteRepository;
import com.example.popularmovies.api.MoviesDbRepository;
import com.example.popularmovies.constants.MovieSortMode;
import com.example.popularmovies.model.Favorite;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.ResultMovies;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {
    public static final int PAGE_SIZE = 20;
    private static final int FIRST_PAGE = 1;

    private MovieSortMode mSortMode=MovieSortMode.SORT_MODE_POPULAR;//"top_rated";

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        //Log.i("Observe",mSortMode);

        if(mSortMode == MovieSortMode.SORT_MODE_POPULAR || mSortMode == MovieSortMode.SORT_MODE_TOPRATED) {
            MoviesDbRepository.getInstance().getMoviesByPageCb(new Callback<ResultMovies>() {
                @Override
                public void onResponse(Call<ResultMovies> call, Response<ResultMovies> response) {

                    if (response != null && response.body() != null) {
                        List<Movie> lm = response.body().getMovies();
                        callback.onResult(lm, null, FIRST_PAGE + 1);
                    }
                }

                @Override
                public void onFailure(Call<ResultMovies> call, Throwable t) {

                }
            }, mSortMode.getKey(), FIRST_PAGE);
        } else if(mSortMode == MovieSortMode.SORT_MODE_FAVORITE){
            List<Favorite> fabs =
                FavoriteRepository.getInstance().getFavoriteLimitOffset(PAGE_SIZE,0);
            Log.i("Observe",fabs.toString());
            if(fabs != null){
                List<Movie> lm = new ArrayList<>();
                for(Favorite fab:fabs){
                    Movie mv = new Movie(fab);
                    Log.i("Observe",mv.getPosterPath()+","+mv.getId()+","+mv.getTitle());
                    lm.add(mv);
                }
                Log.i("Observe",lm.toString());
                callback.onResult(lm, null, FIRST_PAGE + 1);
            }
        }
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
        },mSortMode.getKey(), params.key);
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        Log.i("Observe","loadAfter");
        if(mSortMode == MovieSortMode.SORT_MODE_POPULAR || mSortMode == MovieSortMode.SORT_MODE_TOPRATED) {

            MoviesDbRepository.getInstance().getMoviesByPageCb(new Callback<ResultMovies>() {
                @Override
                public void onResponse(Call<ResultMovies> call, Response<ResultMovies> response) {
                    if (response != null && response.body() != null) {
                        List<Movie> lm = response.body().getMovies();
                        Integer key = lm.size() > 0 ? params.key + 1 : null;
                        callback.onResult(response.body().getMovies(), key);
                    }
                }

                @Override
                public void onFailure(Call<ResultMovies> call, Throwable t) {

                }
            }, mSortMode.getKey(), params.key);
        } else if(mSortMode == MovieSortMode.SORT_MODE_FAVORITE){
            List<Favorite> fabs =
                    FavoriteRepository.getInstance().getFavoriteLimitOffset(PAGE_SIZE,params.key*PAGE_SIZE);
            Log.i("Observe",fabs.toString());
            if(fabs != null){
                List<Movie> lm = new ArrayList<>();
                for(Favorite fab:fabs){
                    lm.add(new Movie(fab));
                }
                Log.i("Observe",lm.toString());
                Integer key = lm.size() > 0 ? params.key + 1 : null;
                callback.onResult(lm, key);
            }

        }
    }

    public void setSortMode(MovieSortMode sortMode){
        mSortMode = sortMode;
    }

}
