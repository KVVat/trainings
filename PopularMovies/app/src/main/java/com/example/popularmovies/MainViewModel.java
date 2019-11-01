package com.example.popularmovies;

import android.content.Context;
import android.util.Log;

import com.example.popularmovies.datasource.MovieDataSource;
import com.example.popularmovies.datasource.MovieDataSourceFactory;
import com.example.popularmovies.model.Movie;

import java.util.List;

import androidx.databinding.ObservableArrayMap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

public class MainViewModel extends ViewModel {

    LiveData<PagedList<Movie>> moviePagedList;
    LiveData<PageKeyedDataSource<Integer, Movie>> liveDataSource;

    PagingMoviesAdapter adapter2;
    public MainViewModel(){
        MovieDataSourceFactory movieDataSourceFactory = new MovieDataSourceFactory();
        //getting the live data source from data source factory
        liveDataSource = movieDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(MovieDataSource.PAGE_SIZE)
                        .build();
        //Building the paged list
        moviePagedList = (new LivePagedListBuilder(movieDataSourceFactory, pagedListConfig))
                //.setInitialKey(0)
                .build();
    }

    public PagingMoviesAdapter getAdapter2() {
        return adapter2;
    }

    public Movie getPagedMovieAt(Integer index) {
        //Log.i("eachMovie",response.getMutableMovies()+","+index);
        if (moviePagedList.getValue() != null &&
                index != null) {
            //Log.i("eachMovie",response.getMutableMovies().getValue().get(index).toString());
            return moviePagedList.getValue().get(index);
        }
        return null;
    }


    //old implement
    private ResponseModel response;
    private MainAdapter adapter;
    public ObservableArrayMap<String, String> images;
    public void init(Context ctx) {
        //dogBreeds = new DogBreeds();
        response = new ResponseModel();
        adapter = new MainAdapter(R.layout.movie_view, this);
        images = new ObservableArrayMap<>();
        adapter2 = new PagingMoviesAdapter(ctx,this);
        //loading = new ObservableInt(View.GONE);
        //showEmpty = new ObservableInt(View.GONE);
    }
    public void fetchList() {
        response.fetchList();
    }
    public MutableLiveData<List<Movie>> getMutableMovies() {
        return response.getMutableMovies();
    }

    public void onItemClick(Integer index) {
        Movie model = getPagedMovieAt(index);
        Log.i("TAG","ID>"+model.getId());
        //selected.setValue(db);
    }
    public MainAdapter getAdapter() {
        return adapter;
    }
    public void setResponseModelInAdapter(List<Movie> model){
        this.adapter.setRecyclerModel(model);
        this.adapter.notifyDataSetChanged();
    }
    public Movie getRecyclerModelAt(Integer index) {
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
        Movie movie = getRecyclerModelAt(index);
        //Log.i("fetch",""+index);
        if (movie != null && !images.containsKey(movie.getId())) {
            String thumbnailUrl = movie.getPosterPath();
            images.put(movie.getId().toString(), thumbnailUrl);
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
