package com.example.android.popmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popmovies.Data.MovieUrlConstants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.ItemClickListener, LoaderManager.LoaderCallbacks<List<Movies>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Constant value for the movie loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    public static final int MOVIE_LOADER_ID = 1;

    /**
     * Adapter for the list of movies
     */
    private MovieRecyclerViewAdapter mRecyclerAdapter;

    private TextView mEmptyStateTextView;

    private ProgressBar mProgressBarView;

    private String mSortOrder;

    private RecyclerView mMovieRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSortOrder = getSortOrderFromPreferences(this);

        // Find a reference to the {@link RecyclerView} in the layout
        mMovieRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_poster_grid);


        mEmptyStateTextView = (TextView) findViewById(R.id.empty);

        mProgressBarView = (ProgressBar) findViewById(R.id.progress);

        mEmptyStateTextView.setVisibility(View.VISIBLE);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            // Create a new adapter that takes an empty list of movies as input
            int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());
            GridLayoutManager mMovieRecyclerViewLayoutManager = new GridLayoutManager(this, mNoOfColumns, LinearLayoutManager.VERTICAL, false);
            mMovieRecyclerView.setLayoutManager(mMovieRecyclerViewLayoutManager);
            mMovieRecyclerView.setHasFixedSize(true);
            mRecyclerAdapter = new MovieRecyclerViewAdapter(this, new ArrayList<Movies>());
            mRecyclerAdapter.setClickListener(this);
            mMovieRecyclerView.setAdapter(mRecyclerAdapter);

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.i(LOG_TAG, "TEST:  Loader initialized");
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);


        } else {
            // Set progress bar and recycler view visibility to gone
            showEmptyState();
            // Set empty state text to display "No internet connection."
            mEmptyStateTextView.setText(R.string.no_internet);

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        String currentSortOrder = getSortOrderFromPreferences(this);
        if (currentSortOrder != mSortOrder) {
            mSortOrder = currentSortOrder;
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        }

    }

    public static String getSortOrderFromPreferences(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForSortOrder = context.getString(R.string.sp_key_sort_order);
        String defaultSortOrder = context.getString(R.string.array_value_most_popular);
        String preferredSortOrder = sp.getString(keyForSortOrder, defaultSortOrder);

        if (preferredSortOrder.equals(defaultSortOrder)) {
            return MovieUrlConstants.SORT_BY_DEFAULT;
        } else {
            return MovieUrlConstants.SORT_BY_HIGHEST_RATED;
        }
    }

    private void launchDetailActivity(Movies currentMovie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.CURRENT_MOVIE, currentMovie);
        startActivity(intent);
    }

    @Override
    public Loader<List<Movies>> onCreateLoader(int i, Bundle bundle) {
        showProgressBar();

        Uri baseUri = Uri.parse(MovieUrlConstants.BASE_SEARCH_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();


        uriBuilder.appendEncodedPath(mSortOrder);
        uriBuilder.appendQueryParameter(MovieUrlConstants.API_PARAM, MovieUrlConstants.API_KEY);
        uriBuilder.appendQueryParameter(MovieUrlConstants.LANGUAGE_PARAM, MovieUrlConstants.DEFAULT_LANGUAGE);


        return new MovieLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movies>> loader, List<Movies> movies) {
        // Clear the adapter of previous movie data
        mRecyclerAdapter.clear();

        // Set progress bar view visibility to gone
        mProgressBarView.setVisibility(View.GONE);

        // Set empty state text to display "No movies found."
        if (movies == null || movies.isEmpty()) {
            showEmptyState();
            mEmptyStateTextView.setText(R.string.error_no_movies_found);
        }
        Log.i(LOG_TAG, "TEST: onLoadFinished() executed");

        // If there is a valid list of {@link Movies}s, then add them to the adapter's
        // data set. This will trigger the RecyclerView Grid to update.
        if (movies != null && !movies.isEmpty()) {
            showMovieGridView();
            mRecyclerAdapter.setMovieData(movies);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movies>> loader) {
        Log.i(LOG_TAG, "TEST: onLoaderReset() executed");
        // Loader reset, so we can clear out our existing data.
        mRecyclerAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            switch (id) {
                case R.id.settings:
                    startActivity(new Intent(this, SettingsActivity.class));
                    return true;
            }
        } else {
            showEmptyState();
            // Set empty state text to display "No internet connection."
            mEmptyStateTextView.setText(R.string.no_internet);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position) {
        Movies currentMovie = mRecyclerAdapter.getItem(position);
        if (currentMovie != null) {
            launchDetailActivity(currentMovie);
        }
    }

    private void showEmptyState() {
        mMovieRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBarView.setVisibility(View.INVISIBLE);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mMovieRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBarView.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setVisibility(View.INVISIBLE);
    }

    private void showMovieGridView() {
        mMovieRecyclerView.setVisibility(View.VISIBLE);
        mProgressBarView.setVisibility(View.INVISIBLE);
        mEmptyStateTextView.setVisibility(View.INVISIBLE);
    }
}









