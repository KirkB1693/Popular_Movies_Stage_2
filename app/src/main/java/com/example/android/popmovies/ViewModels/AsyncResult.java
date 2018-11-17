package com.example.android.popmovies.ViewModels;

import com.example.android.popmovies.RoomDatabase.FavoriteMovieEntry;

import java.util.List;

public interface AsyncResult {
    void asyncFinished(List<FavoriteMovieEntry> results);
}
