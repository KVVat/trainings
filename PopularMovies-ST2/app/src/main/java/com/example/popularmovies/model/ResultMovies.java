package com.example.popularmovies.model;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultMovies extends BaseObservable
{
    @SerializedName("page")
    private Integer page;
    @SerializedName("results")
    private List<Movie> movies;
    @SerializedName("total_pages")
    private Integer pages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

}
