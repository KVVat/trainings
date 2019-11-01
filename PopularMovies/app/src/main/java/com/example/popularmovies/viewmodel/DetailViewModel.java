package com.example.popularmovies.viewmodel;

import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.popularmovies.model.Detail;
import com.example.popularmovies.api.MoviesDbRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewModel extends ViewModel {

    private String TAG="DetailViewModel";

    public ObservableInt errorLayout= new ObservableInt(View.GONE);
    public ObservableInt mainLayout= new ObservableInt(View.GONE);

    private MutableLiveData<Detail> mutableDetail = new MutableLiveData<>();

    public void init() {
        //errorLayout = new ObservableInt(View.GONE);
        //mainLayout = new ObservableInt(View.GONE);
    }

    public LiveData<Detail> getDetail(Long movie_id) {

        MoviesDbRepository.getInstance().getDetail(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                if (response.isSuccessful()) {
                    Detail moviesResponse = response.body();
                    Log.i(TAG,"Load Movie OK."+moviesResponse.getOriginalTitle());

                    mutableDetail.setValue(moviesResponse);
                    errorLayout.set(View.GONE);
                    mainLayout.set(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Log.i(TAG,"Failed to load movie."+t.toString());
                errorLayout.set(View.VISIBLE);
                mainLayout.set(View.GONE);
                mutableDetail.setValue(new Detail());
            }
        },movie_id.toString());
        //}
        return mutableDetail;
    }


}
