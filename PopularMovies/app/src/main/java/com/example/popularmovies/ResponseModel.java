package com.example.popularmovies;

import android.util.Log;

import com.example.popularmovies.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseModel extends BaseObservable{

    MutableLiveData<List<Movie>> mutableMovies = new MutableLiveData<>();

    @SerializedName("page")
    private Integer page;
    @Bindable
    @SerializedName("results")
    List<Movie> movies;
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

    public MutableLiveData<List<Movie>> getMutableMovies() {
        return mutableMovies;
    }

    public void fetchList() {
        //Log.i("fetchList","fetchList Called?");
        MoviesRepository.getInstance().api.getMovies("top_rated",BuildConfig.IMDbAPIKEY,"en-US",1)
            .enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    ResponseModel moviesResponse = response.body();
                    //
                    mutableMovies.setValue(moviesResponse.getMovies());
                    //Log.i("test>success",mutableMovies.getValue().toString());
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("test","failure");
            }
        });
    }
}
