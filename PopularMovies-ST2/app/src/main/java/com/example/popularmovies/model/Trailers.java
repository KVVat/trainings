package com.example.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailers {

    String id;
    @SerializedName("results")
    List<Trailer> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
