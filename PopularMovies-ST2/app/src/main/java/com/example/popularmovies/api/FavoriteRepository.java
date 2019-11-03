package com.example.popularmovies.api;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.popularmovies.applilcation.ApplicationContext;
import com.example.popularmovies.dao.FavoriteDao;
import com.example.popularmovies.model.Favorite;

import java.util.List;

public class FavoriteRepository {
    private FavoriteDao mFavoriteDao;
    private static FavoriteRepository sInstance;
    //private LiveData<List<Favorite>> mAllFavorites;
    FavoriteRepository(Application application) {
        FavoriteDatabase db = FavoriteDatabase.getDatabase(application);
        mFavoriteDao = db.favoriteDao();
        //mAllWords = mWordDao.getAlphabetizedWords();
    }
    public static FavoriteRepository getInstance() {
        if (sInstance == null) {
            synchronized (FavoriteRepository.class) {
                if (sInstance == null) {
                    sInstance = new FavoriteRepository(ApplicationContext.instance);
                }
            }
        }
        return sInstance;
    }

    public List<Favorite> getFavoriteLimitOffset(int limit,int offset){
        return mFavoriteDao.getFavoritesLimitOffset(limit,offset);
        //new favoriteListAsyncTask(mFavoriteDao).execute(offset);

    }
    public Boolean isFavoirte(final Integer movieId) {
        Boolean r = mFavoriteDao.isFavorite(movieId);
        return r;
    }

    public void insert (Favorite favorite) {
        Log.i("Observe","Insert Called "+favorite.getMovieId()+","+favorite.getFavorite());
        new insertAsyncTask(mFavoriteDao).execute(favorite);
    }



    private static class insertAsyncTask extends AsyncTask<Favorite, Void, Void> {

        private FavoriteDao mAsyncTaskDao;

        insertAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Favorite... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
