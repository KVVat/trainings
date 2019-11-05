package com.example.popularmovies.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.R;
import com.example.popularmovies.apdater.ReviewsAdapter;
import com.example.popularmovies.apdater.TrailerAdapter;
import com.example.popularmovies.persistence.FavoriteRepository;
import com.example.popularmovies.api.MoviesDbRepository;
import com.example.popularmovies.constants.Constants;
import com.example.popularmovies.model.Detail;
import com.example.popularmovies.model.Reviews;
import com.example.popularmovies.model.Trailers;
import com.example.popularmovies.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailViewModel extends ViewModel {

    private String TAG="DetailViewModel";

    public ObservableInt loading = new ObservableInt(View.GONE);
    public ObservableInt error= new ObservableInt(View.GONE);
    public ObservableInt mainLayout= new ObservableInt(View.GONE);
    public ObservableBoolean isFavorite = new ObservableBoolean();
    public ObservableBoolean isForceChrome = new ObservableBoolean();

    public MutableLiveData<Detail> mutableDetail = new MutableLiveData<>();
    private TrailerAdapter trailerAdapter;
    private ReviewsAdapter reviewsAdapter;
    private Context mCtx;
    public void init(Context ctx) {
        mCtx = ctx;
        trailerAdapter = new TrailerAdapter(R.layout.trailer_row,this);
        reviewsAdapter = new ReviewsAdapter(R.layout.review_row,this);
        isForceChrome.set(SharedPreferenceUtil.getInstance(ctx).getForceChrome());
    }
    public TrailerAdapter getTrailerAdapter() {
        return trailerAdapter;
    }
    public ReviewsAdapter getReviewsAdapter() {
        return reviewsAdapter;
    }

    public void toggleForceChrome(){
        isForceChrome.set(isForceChrome.get()==true?false:true);
    }

    public void getIsFavorite(Integer movieId){
        //User RxJava2 to implement query.
        Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> emitter) throws Exception {
                Boolean b = FavoriteRepository.getInstance().isFavoirte(movieId);
                emitter.onSuccess(b);
            }
        }).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) { }
            @Override
            public void onSuccess(Boolean aBoolean) {
                isFavorite.set((Boolean)aBoolean);
            }
            @Override
            public void onError(Throwable e) {
                Log.i("Observe","error in fav:"+e.toString());
            }
        });
    }

    public MutableLiveData<Detail> getDetail(Long movie_id) {
        //User RxJava2 to implement call three web api in a row.
        List<Observable<?>> rq = new ArrayList<>();
        String id = movie_id.toString();
        rq.add(MoviesDbRepository.getInstance().getApi().getDetail(
                id, BuildConfig.TMDbAPIKEY, Constants.LANGUAGE,""));
        rq.add(MoviesDbRepository.getInstance().getApi().getTrailers(
                id, BuildConfig.TMDbAPIKEY, Constants.LANGUAGE));
        rq.add(MoviesDbRepository.getInstance().getApi().getReviews(
                id,BuildConfig.TMDbAPIKEY,Constants.LANGUAGE,1));

        loading.set(View.VISIBLE);
        error.set(View.GONE);

        Observable.zip(
            rq,new Function<Object[], Object>() {
            @Override
            public Object apply(Object[] objects) throws Exception {
                Detail detail = new Detail();
                if(objects.length>=2){
                    detail = (Detail)objects[0];
                    detail.setTrailers((Trailers)objects[1]);
                }
                if(objects.length>=3){
                    detail.setReviews((Reviews)objects[2]);
                }
                return detail;
            }}).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        mutableDetail.setValue((Detail)o);
                        error.set(View.GONE);
                        mainLayout.set(View.VISIBLE);
                        loading.set(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Observe","error in dt:"+e.toString()+","+e.getMessage());
                        error.set(View.VISIBLE);
                        mainLayout.set(View.GONE);
                        loading.set(View.GONE);
                        mutableDetail.setValue(new Detail());
                    }

                    @Override
                    public void onComplete() { }
                });

        return mutableDetail;
    }
}
