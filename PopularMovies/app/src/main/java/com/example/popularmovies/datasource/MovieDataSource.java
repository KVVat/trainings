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
    //the size of a page that we want
    public static final int PAGE_SIZE = 20;

    //we will start from the first page which is 1
    private static final int FIRST_PAGE = 1;

    //we need to fetch from stackoverflow
    private static final String SITE_NAME = "stackoverflow";


    //this will be called once to load the initial data
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        Log.i("Observe","LoadInitial");
        Response<ResponseModel> rm = MoviesRepository.getInstance().getMoviesByPage(null,FIRST_PAGE);
        if(rm != null){
            List<Movie> lm = rm.body().getMovies();
            Log.i("Observe",lm.toString());
            callback.onResult(lm,null, FIRST_PAGE+1);
        }
    }

    //this will load the previous page
    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        Log.i("Observe","LoadBefore:");
        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
        Response<ResponseModel> rm = MoviesRepository.getInstance().getMoviesByPage(null,params.key);
        if(rm != null){

            callback.onResult(rm.body().getMovies(),adjacentKey);
        }
    }

    //this will load the next page
    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        Log.i("Observe","LoadAfter");
        Response<ResponseModel> rm = MoviesRepository.getInstance().getMoviesByPage(null,params.key);
        if(rm != null){
            //rm.body().has_more;
            List<Movie> m = rm.body().getMovies();
            Integer key =  m.size()>0? params.key + 1 : null;
            callback.onResult(m,key);
        }
    }

}
