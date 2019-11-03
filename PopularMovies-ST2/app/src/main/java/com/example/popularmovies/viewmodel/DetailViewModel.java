package com.example.popularmovies.viewmodel;

import android.util.Log;
import android.view.View;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.R;
import com.example.popularmovies.apdater.TrailerAdapter;
import com.example.popularmovies.api.FavoriteRepository;
import com.example.popularmovies.api.MoviesDbRepository;
import com.example.popularmovies.constants.Constants;
import com.example.popularmovies.model.Detail;
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.model.Trailers;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DetailViewModel extends ViewModel {

    private String TAG="DetailViewModel";

    public ObservableInt errorLayout= new ObservableInt(View.GONE);
    public ObservableInt mainLayout= new ObservableInt(View.GONE);
    public ObservableBoolean isFavorite = new ObservableBoolean();

    public MutableLiveData<Detail> mutableDetail = new MutableLiveData<>();
    private TrailerAdapter trailerAdapter;
    public void init() {
        trailerAdapter = new TrailerAdapter(R.layout.trailer_row,this);

        //favoriteRepository = FavoriteRepository.getInstance();
    }
    public TrailerAdapter getTrailerAdapter() {
        return trailerAdapter;
    }

    public ObservableBoolean getIsFavorite(Integer movieId){
        Boolean b = FavoriteRepository.getInstance().isFavoirte(movieId);
        isFavorite.set(false);
        Log.i("Observe","get"+b);
        if(b  != null)
            isFavorite.set(b);

        return isFavorite;
    }


    public Trailer getTrailerAt(int pos){
        if(mutableDetail != null) {
            Trailers trailers = mutableDetail.getValue().getTrailers();
            List<Trailer> lists = trailers.getResults();
            if(lists.size() != 0 && lists.size()+1>pos){
                return lists.get(pos);
            }
        }
        return null;
    }
    public MutableLiveData<Detail> getDetail(Long movie_id) {
        Log.i("Observe","getDetail Starts");
        List<Observable<?>> rq = new ArrayList<>();
        String id = movie_id.toString();
        rq.add(MoviesDbRepository.getInstance().getApi().getDetail(
                id, BuildConfig.TMDbAPIKEY, Constants.LANGUAGE,""));
        rq.add(MoviesDbRepository.getInstance().getApi().getTrailers(
                id, BuildConfig.TMDbAPIKEY, Constants.LANGUAGE));

        Observable.zip(
            rq,new Function<Object[], Object>() {
            @Override
            public Object apply(Object[] objects) throws Exception {
                Detail detail = new Detail();
                if(objects.length>=2){
                    detail = (Detail)objects[0];
                    detail.setTrailers((Trailers)objects[1]);
                }
                return (Object)detail;
            }})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                  new Consumer<Object>(){
                      @Override
                      public void accept(Object o) throws Exception {
                          Log.i("Observe","rx:success"+o.toString());
                          mutableDetail.setValue((Detail)o);
                          errorLayout.set(View.GONE);
                          mainLayout.set(View.VISIBLE);
                      }
                  },new Consumer<Object>(){
                        @Override
                        public void accept(Object o) throws Exception {
                            Log.i("Observe","rx:error:"+o.toString());
                            errorLayout.set(View.VISIBLE);
                            mainLayout.set(View.GONE);
                            mutableDetail.setValue(new Detail());
                        }
                    }
             );

        return mutableDetail;
    }
}
