package com.example.popularmovies.datasource;

import com.example.popularmovies.model.Movie;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class MovieDataSourceFactory extends DataSource.Factory{
    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, Movie>> movieLiveDataSource
            = new MutableLiveData<>();

    @Override
    public DataSource<Integer, Movie> create() {
        //getting our data source object
        MovieDataSource itemDataSource = new MovieDataSource();

        //posting the datasource to get the values
        movieLiveDataSource.postValue(itemDataSource);

        //returning the datasource
        return itemDataSource;
    }


    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, Movie>> getItemLiveDataSource() {
        return movieLiveDataSource;
    }

}
