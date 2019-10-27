package com.example.popularmovies;

import com.example.popularmovies.model.Detail;

public interface TMDbGetDetailCallback {
    void onSuccess(Detail detail);
    void onError();
}
