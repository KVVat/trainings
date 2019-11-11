package com.example.popularmovies.viewmodel;


import android.view.View;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.popularmovies.apdater.PagingMoviesAdapter;
import com.example.popularmovies.datasource.MovieDataSource;
import com.example.popularmovies.datasource.MovieDataSourceFactory;
import com.example.popularmovies.model.Movie;


/**
 * viewModel for MainActivity
 */
public class MainViewModel extends ViewModel {

    public LiveData<PagedList<Movie>> moviePagedList;

    public ObservableInt loading = new ObservableInt();
    public ObservableInt showEmpty = new ObservableInt();

    private PagingMoviesAdapter adapter;

    @SuppressWarnings("unchecked")
    public MainViewModel(){
        MovieDataSourceFactory movieDataSourceFactory = new MovieDataSourceFactory();
        //liveDataSource = movieDataSourceFactory.getItemLiveDataSource();
        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(MovieDataSource.PAGE_SIZE)
                        .build();

        moviePagedList = new LivePagedListBuilder(movieDataSourceFactory, pagedListConfig)
                .build();
    }

    public void init() {
        adapter = new PagingMoviesAdapter();
        loading = new ObservableInt(View.GONE);
        showEmpty = new ObservableInt(View.GONE);
    }

    public PagingMoviesAdapter getAdapter() {
        return adapter;
    }

    public DataSource<?,Movie> getMovieDatasource(){
        PagedList<Movie> pagedList = moviePagedList.getValue();
        if(pagedList!=null){
            //DataSource<?, Movie>
            return pagedList.getDataSource();
        }
        return null;
    }
}