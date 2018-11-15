package com.example.android.popmovies.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.popmovies.RoomDatabase.FavoriteMovieRoomDatabase;

public class SingleMovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final FavoriteMovieRoomDatabase mDb;

    private final String mMovieId;

    public SingleMovieViewModelFactory(FavoriteMovieRoomDatabase favoriteMovieRoomDatabase, String movieId) {
        mDb = favoriteMovieRoomDatabase;
        mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SingleMovieViewModel(mDb, mMovieId);
    }
}
