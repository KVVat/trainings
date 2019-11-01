package com.example.popularmovies.datasource;

import android.util.Log;

import com.example.popularmovies.MoviesRepository;
import com.example.popularmovies.ResponseModel;
import com.example.popularmovies.model.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {
    public static final int PAGE_SIZE = 20;
    private static final int FIRST_PAGE = 1;
    private String mSortMode=null;
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        Response<ResponseModel> rm = MoviesRepository.getInstance().getMoviesByPage(mSortMode,FIRST_PAGE);
        if(rm != null && rm.body()!=null){
            List<Movie> lm = rm.body().getMovies();
            Log.i("Observe",lm.toString());
            callback.onResult(lm,null, FIRST_PAGE+1);
        }
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
        Response<ResponseModel> rm = MoviesRepository.getInstance().getMoviesByPage(mSortMode,params.key);
        if(rm != null){

            callback.onResult(rm.body().getMovies(),adjacentKey);
        }
    }

    //this will load the next page
    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        Response<ResponseModel> rm = MoviesRepository.getInstance().getMoviesByPage(mSortMode,params.key);
        if(rm != null){
            //rm.body().has_more;
            List<Movie> m = rm.body().getMovies();
            Integer key =  m.size()>0? params.key + 1 : null;
            callback.onResult(m,key);
        }
    }

    public void setSortMode(String sortMode){
        mSortMode = sortMode;
    }

}
