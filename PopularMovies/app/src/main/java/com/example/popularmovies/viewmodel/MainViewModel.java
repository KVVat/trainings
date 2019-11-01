package com.example.popularmovies.viewmodel;


import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.databinding.Observable;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.popularmovies.apdater.PagingMoviesAdapter;
import com.example.popularmovies.datasource.MovieDataSource;
import com.example.popularmovies.datasource.MovieDataSourceFactory;
import com.example.popularmovies.model.Movie;


public class MainViewModel extends ViewModel {

    private String TAG="MainViewModel";

    public LiveData<PagedList<Movie>> moviePagedList;
    private LiveData<PageKeyedDataSource<Integer, Movie>> liveDataSource;

    public ObservableInt loading;
    public ObservableInt showEmpty;

    private PagingMoviesAdapter adapter;

    public MainViewModel(){
        MovieDataSourceFactory movieDataSourceFactory = new MovieDataSourceFactory();
        liveDataSource = movieDataSourceFactory.getItemLiveDataSource();
        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(MovieDataSource.PAGE_SIZE)
                        .build();
        moviePagedList = new LivePagedListBuilder(movieDataSourceFactory, pagedListConfig)
                .build();
    }

    public void init(Context ctx) {
        adapter = new PagingMoviesAdapter();
        loading = new ObservableInt(View.GONE);
        showEmpty = new ObservableInt(View.GONE);
    }

    public PagingMoviesAdapter getAdapter() {
        return adapter;
    }

    public Movie getPagedMovieAt(Integer index) {
        if (moviePagedList.getValue() != null && index != null) {
            return moviePagedList.getValue().get(index);
        }
        return null;
    }


}