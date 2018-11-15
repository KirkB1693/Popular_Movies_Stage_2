package com.example.android.popmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popmovies.RoomDatabase.FavoriteMovieEntry;
import com.example.android.popmovies.RoomDatabase.FavoriteMovieRoomDatabase;

public class SingleMovieViewModel extends ViewModel {

    private LiveData<FavoriteMovieEntry> favoriteMovieEntryLiveData;

    public SingleMovieViewModel(FavoriteMovieRoomDatabase favoriteMovieRoomDatabase, String movieId) {
        favoriteMovieEntryLiveData = favoriteMovieRoomDatabase.favoriteMovieDao().loadMovieById(movieId);
    }

    public LiveData<FavoriteMovieEntry> getFavoriteMovieEntryLiveData() {return favoriteMovieEntryLiveData;}
}
