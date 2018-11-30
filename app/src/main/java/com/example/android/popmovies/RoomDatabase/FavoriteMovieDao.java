package com.example.android.popmovies.RoomDatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.popmovies.Data.MovieContract;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM "+ MovieContract.MovieEntry.TABLE_NAME + " ORDER BY CAST(" + MovieContract.MovieEntry.COLUMN_POPULARITY + " AS REAL) DESC")
    LiveData<List<FavoriteMovieEntry>> loadAllMoviesByPopularity();

    @Query("SELECT * FROM " + MovieContract.MovieEntry.TABLE_NAME + " ORDER BY CAST(" + MovieContract.MovieEntry.COLUMN_USER_RATING + " AS REAL) DESC")
    LiveData<List<FavoriteMovieEntry>> loadAllMoviesByUserRating();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(FavoriteMovieEntry favoriteMovieEntry);

    @Query("DELETE FROM " + MovieContract.MovieEntry.TABLE_NAME + " WHERE "+ MovieContract.MovieEntry.COLUMN_MOVIE_ID +" = :id")
    void deleteMovie(String id);

    @Query("SELECT * FROM " + MovieContract.MovieEntry.TABLE_NAME + " WHERE "+ MovieContract.MovieEntry.COLUMN_MOVIE_ID +" = :id")
    List<FavoriteMovieEntry> loadMovieById(String id);
}

