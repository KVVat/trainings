package com.example.popularmovies;

import android.util.Log;

import java.util.List;

import androidx.databinding.ObservableArrayMap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private ResponseModel response;
    private MainAdapter adapter;
    public ObservableArrayMap<String, String> images;
    public void init() {
        //dogBreeds = new DogBreeds();
        response = new ResponseModel();
        adapter = new MainAdapter(R.layout.movie_view, this);
        images = new ObservableArrayMap<>();
        //loading = new ObservableInt(View.GONE);
        //showEmpty = new ObservableInt(View.GONE);
    }
    public void fetchList() {
        response.fetchList();
    }
    public MutableLiveData<List<RecyclerModel>> getMutableMovies() {
        return response.getMutableMovies();
    }

    public void onItemClick(Integer index) {
        RecyclerModel model = getRecyclerModelAt(index);
        Log.i("TAG","ID>"+model.getId());
        //selected.setValue(db);
    }
    public MainAdapter getAdapter() {
        return adapter;
    }
    public void setResponseModelInAdapter(List<RecyclerModel> model){
        this.adapter.setRecyclerModel(model);
        this.adapter.notifyDataSetChanged();
    }
    public RecyclerModel getRecyclerModelAt(Integer index) {
        //Log.i("eachMovie",response.getMutableMovies()+","+index);
        if (response.getMutableMovies().getValue() != null &&
                index != null &&
                response.getMutableMovies().getValue().size() > index) {
            //Log.i("eachMovie",response.getMutableMovies().getValue().get(index).toString());
            return response.getMutableMovies().getValue().get(index);
        }
        return null;
    }
    public void fetchMovieImagesAt(Integer index) {
        RecyclerModel recyclerModel = getRecyclerModelAt(index);
        //Log.i("fetch",""+index);
        if (recyclerModel != null && !images.containsKey(recyclerModel.getId())) {
            String thumbnailUrl = recyclerModel.getPosterPath();
            images.put(recyclerModel.getId().toString(), thumbnailUrl);
            //Log.i("test",thumbnailUrl);
            /*dogBreed.fetchImages(new DogImagesCallback(dogBreed.getBreed()) {
                @Override
                public void onResponse(Call<DogBreedImages> call, Response<DogBreedImages> response) {
                    DogBreedImages body = response.body();
                    if (body.getImages() != null && body.getImages().length > 0) {
                        String thumbnailUrl = body.getImages()[0];
                        images.put(getBreed(), thumbnailUrl);
                    }
                }

                @Override
                public void onFailure(Call<DogBreedImages> call, Throwable t) {
                    Log.e("Test", t.getMessage(), t);
                }
            });*/
        }
    }

}
