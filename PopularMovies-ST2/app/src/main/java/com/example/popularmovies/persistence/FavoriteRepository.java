package com.example.popularmovies.persistence;

import android.app.Application;
import android.os.AsyncTask;

import com.example.popularmovies.applilcation.ApplicationContext;
import com.example.popularmovies.model.Favorite;

import java.util.List;

/**
 * Handle Room Database access.
 */
public class FavoriteRepository {
    private FavoriteDao mFavoriteDao;

    private static FavoriteRepository sInstance;

    private FavoriteRepository(Application application) {
        FavoriteDatabase db = FavoriteDatabase.getDatabase(application);
        mFavoriteDao = db.favoriteDao();
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

    public Integer getFavoriteCount(){
        return mFavoriteDao.getFavoriteCount();
    }

    public List<Favorite> getFavoriteLimitOffset(int limit,int offset){
        return mFavoriteDao.getFavoritesLimitOffset(limit,offset);
    }

    public Boolean isFavoirte(final Integer movieId) {
        return  mFavoriteDao.isFavorite(movieId);
    }

    public void insert (Favorite favorite) {
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
