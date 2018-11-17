package com.example.android.popmovies.ViewModels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.example.android.popmovies.RoomDatabase.FavoriteMovieDao;
import com.example.android.popmovies.RoomDatabase.FavoriteMovieEntry;
import com.example.android.popmovies.RoomDatabase.FavoriteMovieRoomDatabase;

import java.util.List;

public class FavoriteMovieRepository implements AsyncResult {
    private FavoriteMovieDao mFavoriteMovieDao;
    private LiveData<List<FavoriteMovieEntry>> mAllMoviesByPopularity;
    private LiveData<List<FavoriteMovieEntry>> mAllMoviesByHighestRated;
    private MutableLiveData<List<FavoriteMovieEntry>> mSearchResults = new MutableLiveData<>();

    FavoriteMovieRepository(Application application) {
        FavoriteMovieRoomDatabase db = FavoriteMovieRoomDatabase.getInstance(application);
        mFavoriteMovieDao = db.favoriteMovieDao();

        mAllMoviesByPopularity = mFavoriteMovieDao.loadAllMoviesByPopularity();
        mAllMoviesByHighestRated = mFavoriteMovieDao.loadAllMoviesByUserRating();

    }

    public void insertFavoriteMovie(FavoriteMovieEntry newFavoriteEntry) {
        new queryAsyncTask.insertFavoriteMovieAsyncTask(mFavoriteMovieDao).execute(newFavoriteEntry);
    }

    public void deleteFavoriteMovie(String name) {
        new queryAsyncTask.deleteFavoriteMovieAsyncTask(mFavoriteMovieDao).execute(name);
    }

    public void findFavoriteMovie(String name) {
        queryAsyncTask task = new queryAsyncTask(mFavoriteMovieDao);
        task.delegate = this;
        task.execute(name);
    }

    public LiveData<List<FavoriteMovieEntry>> getAllMoviesByPopularity() {
        return mAllMoviesByPopularity;
    }

    public LiveData<List<FavoriteMovieEntry>> getAllMoviesByHighestRated() {
        return mAllMoviesByHighestRated;
    }

    public MutableLiveData<List<FavoriteMovieEntry>> getSearchResults() {
        return mSearchResults;
    }

    @Override
    public void asyncFinished (List<FavoriteMovieEntry> results){
        mSearchResults.setValue(results);
    }

    private static class queryAsyncTask extends
            AsyncTask<String, Void, List<FavoriteMovieEntry>> {

        private FavoriteMovieDao asyncTaskDao;
        private FavoriteMovieRepository delegate = null;

        queryAsyncTask(FavoriteMovieDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected List<FavoriteMovieEntry> doInBackground(final String... params) {
            return asyncTaskDao.loadMovieById(params[0]);
        }

        @Override
        protected void onPostExecute(List<FavoriteMovieEntry> result) {
            delegate.asyncFinished(result);
        }

        private static class insertFavoriteMovieAsyncTask extends AsyncTask<FavoriteMovieEntry, Void, Void> {

            private FavoriteMovieDao asyncTaskDao;

            insertFavoriteMovieAsyncTask(FavoriteMovieDao dao) {
                asyncTaskDao = dao;
            }

            @Override
            protected Void doInBackground(final FavoriteMovieEntry... params) {
                asyncTaskDao.insertMovie(params[0]);
                return null;
            }
        }

        private static class deleteFavoriteMovieAsyncTask extends AsyncTask<String, Void, Void> {

            private FavoriteMovieDao asyncTaskDao;

            deleteFavoriteMovieAsyncTask(FavoriteMovieDao dao) {
                asyncTaskDao = dao;
            }

            @Override
            protected Void doInBackground(final String... params) {
                asyncTaskDao.deleteMovie(params[0]);
                return null;
            }
        }
    }








}
