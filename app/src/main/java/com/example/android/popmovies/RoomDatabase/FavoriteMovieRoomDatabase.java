package com.example.android.popmovies.RoomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.android.popmovies.Data.MovieContract;

@Database(entities = {FavoriteMovieEntry.class}, version = 1)
public abstract class FavoriteMovieRoomDatabase extends RoomDatabase {

    private static final String LOG_TAG = FavoriteMovieRoomDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = MovieContract.MovieEntry.TABLE_NAME;
    private static FavoriteMovieRoomDatabase sInstance;

    public abstract FavoriteMovieDao favoriteMovieDao();

    public static FavoriteMovieRoomDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (FavoriteMovieRoomDatabase.class) {
                if (sInstance == null) {
                    Log.d(LOG_TAG, "Creating new database instance");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteMovieRoomDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }


}
