package com.example.android.popmovies.RoomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.example.android.popmovies.Data.MovieContract;

@Database(entities = {FavoriteMovieEntry.class}, version = 1, exportSchema = false)
@TypeConverters(ListConverter.class)
public abstract class FavoriteMovieRoomDatabase extends RoomDatabase {

    private static final String LOG_TAG = FavoriteMovieRoomDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = MovieContract.MovieEntry.TABLE_NAME;
    private static FavoriteMovieRoomDatabase sInstance;

    public static FavoriteMovieRoomDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteMovieRoomDatabase.class, FavoriteMovieRoomDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract FavoriteMovieDao favoriteMovieDao();

}
