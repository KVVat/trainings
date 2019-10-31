package com.example.popularmovies;

import com.example.popularmovies.model.Movie;

import java.util.List;

public interface TMDbGetMoviesCallback {
    void onSuccess(List<Movie> movies);
    void onError();
}
