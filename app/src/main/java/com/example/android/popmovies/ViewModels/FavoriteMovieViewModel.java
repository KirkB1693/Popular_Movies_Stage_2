package com.example.android.popmovies.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.popmovies.RoomDatabase.FavoriteMovieEntry;

import java.util.List;

public class FavoriteMovieViewModel extends AndroidViewModel {

    private FavoriteMovieRepository mRepository;

    private LiveData<List<FavoriteMovieEntry>> mAllMoviesByPopularity;
    private LiveData<List<FavoriteMovieEntry>> mAllMoviesByHighestRated;


    public FavoriteMovieViewModel(Application application) {
        super(application);
        mRepository = new FavoriteMovieRepository(application);
        mAllMoviesByPopularity = mRepository.getAllMoviesByPopularity();
        mAllMoviesByHighestRated = mRepository.getAllMoviesByHighestRated();
    }

    public LiveData<List<FavoriteMovieEntry>> getAllMoviesByPopularity() { return mAllMoviesByPopularity; }
    public LiveData<List<FavoriteMovieEntry>> getAllMoviesByHighestRated() { return mAllMoviesByHighestRated; }


    public void insertMovie(FavoriteMovieEntry favoriteMovieEntry) {mRepository.insertMovie(favoriteMovieEntry);}

    public void deleteMovie(FavoriteMovieEntry favoriteMovieEntry) {mRepository.deleteMovie(favoriteMovieEntry);}
}
