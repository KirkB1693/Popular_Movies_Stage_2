package com.example.android.popmovies.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.android.popmovies.RoomDatabase.FavoriteMovieEntry;

import java.util.List;

public class FavoriteMovieViewModel extends AndroidViewModel {

    private FavoriteMovieRepository mRepository;

    private LiveData<List<FavoriteMovieEntry>> mAllMoviesByPopularity;
    private LiveData<List<FavoriteMovieEntry>> mAllMoviesByHighestRated;
    private MutableLiveData<List<FavoriteMovieEntry>> mSearchResults;


    public FavoriteMovieViewModel(Application application) {
        super(application);
        mRepository = new FavoriteMovieRepository(application);
        mAllMoviesByPopularity = mRepository.getAllMoviesByPopularity();
        mAllMoviesByHighestRated = mRepository.getAllMoviesByHighestRated();
        mSearchResults = mRepository.getSearchResults();
    }

    public MutableLiveData<List<FavoriteMovieEntry>> getSearchResults() { return mSearchResults; }

    public LiveData<List<FavoriteMovieEntry>> getAllMoviesByPopularity() { return mAllMoviesByPopularity; }
    public LiveData<List<FavoriteMovieEntry>> getAllMoviesByHighestRated() { return mAllMoviesByHighestRated; }

    public void findMovie(String favoriteMovieId) {mRepository.findFavoriteMovie(favoriteMovieId);}

    public void insertMovie(FavoriteMovieEntry favoriteMovieEntry) {mRepository.insertFavoriteMovie(favoriteMovieEntry);}

    public void deleteMovie(String favoriteMovieId) {mRepository.deleteFavoriteMovie(favoriteMovieId);}

}
