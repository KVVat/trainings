package com.example.popularmovies.datasource;

import com.example.popularmovies.model.Movie;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class MovieDataSourceFactory extends DataSource.Factory{
    private MutableLiveData<PageKeyedDataSource<Integer, Movie>> movieLiveDataSource
            = new MutableLiveData<>();
    public static String mSort = "popular";
    @Override
    public DataSource<Integer, Movie> create() {
        MovieDataSource itemDataSource = new MovieDataSource();
        itemDataSource.setSortMode(mSort);
        movieLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Movie>> getItemLiveDataSource() {
        return movieLiveDataSource;
    }

}
