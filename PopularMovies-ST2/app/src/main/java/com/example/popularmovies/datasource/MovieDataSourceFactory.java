package com.example.popularmovies.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.popularmovies.model.Movie;


public class MovieDataSourceFactory extends DataSource.Factory{
    private MutableLiveData<PageKeyedDataSource<Integer, Movie>> movieLiveDataSource
            = new MutableLiveData<>();

    private static String mSort = "popular";
    public static void setSort(String sort){ mSort = sort;}

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
