package com.example.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;

public class Movie extends BaseObservable {

    public Movie(String title,long id){
        this.id = id;
        this.title = title;
    }
    public Movie(Favorite fav){
        this.id = fav.getMovieId().longValue();
        this.posterPath = fav.getPosterPath();
        this.title = fav.getTitle();
    }

    private Long id;
    private String title;
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("vote_average")
    private Float rating;
    @SerializedName("relese_date")
    private String releaseDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
