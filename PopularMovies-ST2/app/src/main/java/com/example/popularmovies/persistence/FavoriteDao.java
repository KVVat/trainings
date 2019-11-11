package com.example.popularmovies.persistence;

import com.example.popularmovies.model.Favorite;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
public interface FavoriteDao {

    @Query("SELECT COUNT(*) from favorite where is_favorite=1")
    Integer getFavoriteCount();

    @Query("SELECT * from favorite  where is_favorite=1 ORDER BY movie_id LIMIT :limit OFFSET :offset")
    List<Favorite> getFavoritesLimitOffset(Integer limit,Integer offset);


    @Query("SELECT is_favorite from favorite where movie_id=:movieId")
    Boolean isFavorite(final Integer movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Favorite favorite);

}
