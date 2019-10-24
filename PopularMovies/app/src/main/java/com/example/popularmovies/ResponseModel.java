package com.example.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseModel {
    @SerializedName("page")
    private Integer page;
    @SerializedName("results")
    List<RecyclerModel> movies;
    @SerializedName("total_pages")
    private Integer pages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<RecyclerModel> getMovies() {
        return movies;
    }

    public void setMovies(List<RecyclerModel> movies) {
        this.movies = movies;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}
