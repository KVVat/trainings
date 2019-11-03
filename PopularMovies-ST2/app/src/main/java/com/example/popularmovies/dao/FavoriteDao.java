package com.example.popularmovies.dao;

import com.example.popularmovies.model.Favorite;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FavoriteDao {

    @Query("SELECT COUNT(*) from favorite where is_favorite=1")
    public Integer getFavoriteCount();

    @Query("SELECT movie_id from favorite  where is_favorite=1 ORDER BY movie_id LIMIT :limit OFFSET :offset")
    public List<Integer> getFavoritesLimitOffset(Integer limit,Integer offset);

    //fun getUsersLimitOffset(limit: Int, offset: Int): List<User>


    @Query("SELECT is_favorite from favorite where movie_id=:movieId")
    public Boolean isFavorite(final Integer movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Favorite favorite);
    @Update()
    public void update(Favorite favorite);

}
