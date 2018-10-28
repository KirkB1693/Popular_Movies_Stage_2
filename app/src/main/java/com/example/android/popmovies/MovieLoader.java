package com.example.android.popmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

class MovieLoader extends AsyncTaskLoader<List<Movies>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = MovieLoader.class.getName();

    /**
     * Query URL
     */
    private final String mUrl;

    /**
     * Constructs a new {@link MovieLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: onStartLoading()");
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Movies> loadInBackground() {
        Log.i(LOG_TAG, "TEST: loadInBackground()");
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of movies.
        return QueryUtils.extractMovies(mUrl);
    }

}



