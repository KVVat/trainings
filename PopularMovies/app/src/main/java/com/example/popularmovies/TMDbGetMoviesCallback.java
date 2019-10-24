package com.example.popularmovies;

import java.util.List;

public interface TMDbGetMoviesCallback {
    void onSuccess(List<RecyclerModel> movies);
    void onError();
}
