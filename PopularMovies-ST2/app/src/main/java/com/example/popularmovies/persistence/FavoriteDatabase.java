package com.example.popularmovies.persistence;

import android.content.Context;

import com.example.popularmovies.model.Favorite;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Favorite.class}, version = 2)
abstract class FavoriteDatabase extends RoomDatabase {
    public abstract FavoriteDao favoriteDao();
    private static volatile FavoriteDatabase INSTANCE;
    static FavoriteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavoriteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteDatabase.class, "favoite_database")
                            //.allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
