package com.example.android.popmovies.RoomDatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.popmovies.Data.MovieContract;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM "+ MovieContract.MovieEntry.TABLE_NAME + " ORDER BY " + MovieContract.MovieEntry.COLUMN_POPULARITY)
    LiveData<List<FavoriteMovieEntry>> loadAllMoviesByPopularity();

    @Query("SELECT * FROM " + MovieContract.MovieEntry.TABLE_NAME + " ORDER BY " + MovieContract.MovieEntry.COLUMN_USER_RATING)
    LiveData<List<FavoriteMovieEntry>> loadAllMoviesByUserRating();

    @Insert
    void insertMovie(FavoriteMovieEntry favoriteMovieEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(FavoriteMovieEntry favoriteMovieEntry);

    @Delete
    void deleteMovie(FavoriteMovieEntry favoriteMovieEntry);

    @Query("SELECT * FROM " + MovieContract.MovieEntry.TABLE_NAME + " WHERE "+ MovieContract.MovieEntry.COLUMN_MOVIE_ID +" = :id")
    FavoriteMovieEntry loadMovieById(String id);
}

