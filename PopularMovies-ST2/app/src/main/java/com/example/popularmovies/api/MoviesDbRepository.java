package com.example.popularmovies.api;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.constants.Constants;
import com.example.popularmovies.interfaces.TMDbInterface;
import com.example.popularmovies.model.Detail;
import com.example.popularmovies.model.ResultMovies;
import com.example.popularmovies.model.Reviews;
import com.example.popularmovies.model.Trailers;

import io.reactivex.Observable;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Interface of the movie database website api via retrofit2 library.
 */
public class MoviesDbRepository {

    private TMDbInterface api;
    private MoviesDbRepository(TMDbInterface api) {
        this.api = api;
    }

    private static MoviesDbRepository repository;

    /**
     *
     * @return repository instance
     */
    public static MoviesDbRepository getInstance() {
        if (repository == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesDbRepository(retrofit.create(TMDbInterface.class));
        }
        return repository;
    }

    public TMDbInterface getApi(){
        return this.api;
    }

    public void getMoviesByPageCb(Callback<ResultMovies> callback,String sort, int page){
        api.getMovies(sort, BuildConfig.TMDbAPIKEY, Constants.LANGUAGE, page).enqueue(callback);
    }

    public Observable<Detail> getDetail(String id){
        return api.getDetail(id, BuildConfig.TMDbAPIKEY, Constants.LANGUAGE,"");
    }

    public Observable<Trailers> getTrailers(String id){
        return api.getTrailers(id, BuildConfig.TMDbAPIKEY, "");
    }

    public Observable<Reviews> getReviews(String id,int page){
        return api.getReviews(id,BuildConfig.TMDbAPIKEY,Constants.LANGUAGE,page);
    }

}
