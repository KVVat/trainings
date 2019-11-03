package com.example.popularmovies.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class Favorite {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movie_id")
    private Integer movieId;
    @ColumnInfo(name = "is_favorite")
    private Boolean isFavorite;

    public Favorite(Integer movieId,Boolean isFavorite){
        this.movieId = movieId;
        this.isFavorite = isFavorite;
    }

    @NonNull
    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull Integer movieId) {
        this.movieId = movieId;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }
}
