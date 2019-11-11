package com.example.popularmovies.datasource;

import com.example.popularmovies.api.MoviesDbRepository;
import com.example.popularmovies.constants.MovieSortMode;
import com.example.popularmovies.model.Favorite;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.ResultMovies;
import com.example.popularmovies.persistence.FavoriteRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Page DataSource for RecyclerView MainActivity.
 * It supports to handle both network and database activities.
 */
public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {
    public static final int PAGE_SIZE = 20;
    private static final int FIRST_PAGE = 1;

    private MovieSortMode mSortMode=MovieSortMode.SORT_MODE_POPULAR;

    /**
     * Helper for Favoirte items. This method loads Favorite data form repository then turn them into Movie type.
     * @param offset offset of favorite list
     * @return Movie type list that made up by favorite list.
     */
    private List<Movie> favoriteToMovieList(Integer offset){
        List<Favorite> favorites = FavoriteRepository.getInstance().getFavoriteLimitOffset(PAGE_SIZE,offset);
        if(favorites != null){
            List<Movie> lm = new ArrayList<>();
            for(Favorite fab:favorites){
                Movie mv = new Movie(fab);
                lm.add(mv);
            }
            return lm;
        }
        return new ArrayList<>();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        if(mSortMode == MovieSortMode.SORT_MODE_POPULAR || mSortMode == MovieSortMode.SORT_MODE_TOPRATED) {
            MoviesDbRepository.getInstance().getMoviesByPageCb(new Callback<ResultMovies>() {
                @Override
                public void onResponse(@NonNull Call<ResultMovies> call, @NonNull Response<ResultMovies> response) {
                    if (response.body() != null) {
                        List<Movie> lm = response.body().getMovies();
                        callback.onResult(lm, null, FIRST_PAGE + 1);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ResultMovies> call,@NonNull Throwable t) { }
            }, mSortMode.getKey(), FIRST_PAGE);
        } else if(mSortMode == MovieSortMode.SORT_MODE_FAVORITE){

            List<Movie> lm = favoriteToMovieList(0);
            callback.onResult(lm, null, 1);
        }
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {

        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;

        if(mSortMode == MovieSortMode.SORT_MODE_POPULAR || mSortMode == MovieSortMode.SORT_MODE_TOPRATED) {
            MoviesDbRepository.getInstance().getMoviesByPageCb(new Callback<ResultMovies>() {
                @Override
                public void onResponse(@NonNull Call<ResultMovies> call, @NonNull Response<ResultMovies> response) {
                    if (response.body() != null) {
                        List<Movie> lm = response.body().getMovies();
                        callback.onResult(lm, adjacentKey);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ResultMovies> call,@NonNull Throwable t) { }
            }, mSortMode.getKey(), params.key);
        } else if(mSortMode == MovieSortMode.SORT_MODE_FAVORITE){
            List<Movie> lm = favoriteToMovieList(params.key*PAGE_SIZE);
            callback.onResult(lm, adjacentKey);
        }
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        //Log.i("Observe","loadAfter");
        if(mSortMode == MovieSortMode.SORT_MODE_POPULAR || mSortMode == MovieSortMode.SORT_MODE_TOPRATED) {

            MoviesDbRepository.getInstance().getMoviesByPageCb(new Callback<ResultMovies>() {
                @Override
                public void onResponse(@NonNull Call<ResultMovies> call,@NonNull Response<ResultMovies> response) {
                    if (response.body() != null) {
                        List<Movie> lm = response.body().getMovies();
                        Integer key = lm.size() > 0 ? params.key + 1 : null;
                        callback.onResult(response.body().getMovies(), key);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultMovies> call,@NonNull Throwable t) {

                }
            }, mSortMode.getKey(), params.key);
        } else if(mSortMode == MovieSortMode.SORT_MODE_FAVORITE){

            List<Movie> lm = favoriteToMovieList(params.key*PAGE_SIZE);

            int total = FavoriteRepository.getInstance().getFavoriteCount();
            Integer key = null;
            if(params.key*PAGE_SIZE < total){
                key = params.key+1;
            }
            callback.onResult(lm, key);
        }
    }

    //Called by data source factory
    void setSortMode(MovieSortMode sortMode){
        mSortMode = sortMode;
    }

}
