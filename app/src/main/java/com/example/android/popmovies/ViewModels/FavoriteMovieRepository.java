package com.example.android.popmovies.ViewModels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.android.popmovies.RoomDatabase.FavoriteMovieDao;
import com.example.android.popmovies.RoomDatabase.FavoriteMovieEntry;
import com.example.android.popmovies.RoomDatabase.FavoriteMovieRoomDatabase;

import java.util.List;

public class FavoriteMovieRepository {
    private FavoriteMovieDao mFavoriteMovieDao;
    private LiveData<List<FavoriteMovieEntry>> mAllMoviesByPopularity;
    private LiveData<List<FavoriteMovieEntry>> mAllMoviesByHighestRated;

    FavoriteMovieRepository(Application application) {
        FavoriteMovieRoomDatabase db = FavoriteMovieRoomDatabase.getInstance(application);
        mFavoriteMovieDao = db.favoriteMovieDao();
        mAllMoviesByPopularity = mFavoriteMovieDao.loadAllMoviesByPopularity();
        mAllMoviesByHighestRated = mFavoriteMovieDao.loadAllMoviesByUserRating();
    }

    LiveData<List<FavoriteMovieEntry>> getAllMoviesByPopularity() {
        return mAllMoviesByPopularity;
    }

    LiveData<List<FavoriteMovieEntry>> getAllMoviesByHighestRated() {
        return mAllMoviesByHighestRated;
    }

    public void insertMovie (FavoriteMovieEntry favoriteMovieEntry) {
        new insertAsyncTask(mFavoriteMovieDao).execute(favoriteMovieEntry);
    }

    private static class insertAsyncTask extends AsyncTask<FavoriteMovieEntry, Void, Void> {

        private FavoriteMovieDao mAsyncTaskDao;

        insertAsyncTask(FavoriteMovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavoriteMovieEntry... params) {
            mAsyncTaskDao.insertMovie(params[0]);
            return null;
        }
    }

    public void deleteMovie (FavoriteMovieEntry favoriteMovieEntry) {
        new deleteAsyncTask(mFavoriteMovieDao).execute(favoriteMovieEntry);
    }

    private static class deleteAsyncTask extends AsyncTask<FavoriteMovieEntry, Void, Void> {

        private FavoriteMovieDao mAsyncTaskDao;

        deleteAsyncTask(FavoriteMovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavoriteMovieEntry... params) {
            mAsyncTaskDao.deleteMovie(params[0]);
            return null;
        }
    }
}
