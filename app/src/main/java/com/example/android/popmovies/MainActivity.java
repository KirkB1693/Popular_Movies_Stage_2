package com.example.android.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popmovies.Adapters.MovieRecyclerViewAdapter;
import com.example.android.popmovies.Data.MovieUrlConstants;
import com.example.android.popmovies.JsonResponseModels.MoviesModel;
import com.example.android.popmovies.JsonResponseModels.MoviesResponse;
import com.example.android.popmovies.Utilities.ApiClient;
import com.example.android.popmovies.Utilities.ApiService;
import com.example.android.popmovies.Utilities.CalcNumOfColumns;
import com.example.android.popmovies.Utilities.ConnectedToInternet;
import com.example.android.popmovies.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, MovieRecyclerViewAdapter.ItemClickListener {

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

    private boolean mDisplayFavorites;

    ActivityMainBinding mMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mDisplayFavorites = getDisplayFavoritesFromPreferences(this);

        mMainBinding.empty.setVisibility(View.VISIBLE);

        int mNoOfColumns = CalcNumOfColumns.calculateNoOfColumns(getApplicationContext());
        GridLayoutManager mMovieRecyclerViewLayoutManager = new GridLayoutManager(this, mNoOfColumns, LinearLayoutManager.VERTICAL, false);
        mMainBinding.rvMoviePosterGrid.setLayoutManager(mMovieRecyclerViewLayoutManager);
        mMainBinding.rvMoviePosterGrid.setHasFixedSize(true);
        mRecyclerAdapter = new MovieRecyclerViewAdapter(this, new ArrayList<MoviesModel>());
        mRecyclerAdapter.setClickListener(this);
        mMainBinding.rvMoviePosterGrid.setAdapter(mRecyclerAdapter);

        if (ConnectedToInternet.isConnectedToInternet(this) && !mDisplayFavorites) {
            // Create a new adapter that takes an empty list of movies as input
            updateUiFromWeb();

        } else if (mDisplayFavorites) {
            updateUiFromFavorites();
        } else {
            // Set progress bar and recycler view visibility to gone
            showEmptyState();
            // Set empty state text to display "No internet connection."
            mMainBinding.empty.setText(R.string.no_internet);

        }


    }

    private void updateUiFromFavorites() {
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPreferences();

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

    public static boolean getDisplayFavoritesFromPreferences(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForDisplayFavorites = context.getString(R.string.sp_key_show_favorites);
        boolean defaultDisplayFavorites = context.getResources().getBoolean(R.bool.pref_show_favorite_default);

        return sp.getBoolean(keyForDisplayFavorites, defaultDisplayFavorites);
    }

    private void launchDetailActivity(MoviesModel currentMovie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.CURRENT_MOVIE, currentMovie);
        startActivity(intent);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(LOG_TAG, "Preferences updated");
        checkPreferences();
    }

    private void checkPreferences() {
        mDisplayFavorites = getDisplayFavoritesFromPreferences(this);

        if (ConnectedToInternet.isConnectedToInternet(this) && !mDisplayFavorites) {
            // Create a new adapter that takes an empty list of movies as input
            updateUiFromWeb();

        } else if (mDisplayFavorites) {
            updateUiFromFavorites();
        } else {
            // Set progress bar and recycler view visibility to gone
            showEmptyState();
            // Set empty state text to display "No internet connection."
            mMainBinding.empty.setText(R.string.no_internet);

        }
    }



    public void updateUiFromWeb() {
        // Clear the adapter of previous movie data
        mRecyclerAdapter.clear();
        // Set progress bar view visibility to gone
        mMainBinding.progress.setVisibility(View.GONE);

        loadMoviesFromWeb();

        Log.i(LOG_TAG, "TEST: onLoadFinished() executed");


    }

    private void loadMoviesFromWeb() {
        try {
            if (BuildConfig.API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "API Key not found!!! Please obtain API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }

            showProgressBar();
            Retrofit retrofit = ApiClient.getClient();
            ApiService apiService = retrofit.create(ApiService.class);

            String sortPreference = getSortOrderFromPreferences(this);
            Call<MoviesResponse> call = null;
            if (sortPreference.equals(MovieUrlConstants.SORT_BY_DEFAULT)) {
                call = apiService.getPopularMovies(BuildConfig.API_KEY);
            } else {
                call = apiService.getTopRatedMovies(BuildConfig.API_KEY);
            }

            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<MoviesModel> moviesModelList = response.body().getResults();
                    // Set empty state text to display "No movies found."
                    if (moviesModelList == null || moviesModelList.isEmpty()) {
                        showEmptyState();
                        mMainBinding.empty.setText(R.string.error_no_movies_found);
                    }
                    // If there is a valid list of {@link MoviesModel}s, then add them to the adapter's
                    // data set. This will trigger the RecyclerView Grid to update.
                    if (moviesModelList != null && !moviesModelList.isEmpty()) {
                        showMovieGridView();
                        mRecyclerAdapter.setMovieData(moviesModelList);
                    }


                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (ConnectedToInternet.isConnectedToInternet(this)) {
            switch (id) {
                case R.id.settings:
                    startActivity(new Intent(this, SettingsActivity.class));
                    return true;
            }
        } else {
            showEmptyState();
            // Set empty state text to display "No internet connection."
            mMainBinding.empty.setText(R.string.no_internet);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position) {
        MoviesModel currentMovie = mRecyclerAdapter.getItem(position);
        if (currentMovie != null) {
            launchDetailActivity(currentMovie);
        }
    }

    private void showEmptyState() {
        mMainBinding.rvMoviePosterGrid.setVisibility(View.INVISIBLE);
        mMainBinding.progress.setVisibility(View.INVISIBLE);
        mMainBinding.empty.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mMainBinding.rvMoviePosterGrid.setVisibility(View.INVISIBLE);
        mMainBinding.progress.setVisibility(View.VISIBLE);
        mMainBinding.empty.setVisibility(View.INVISIBLE);
    }

    private void showMovieGridView() {
        mMainBinding.rvMoviePosterGrid.setVisibility(View.VISIBLE);
        mMainBinding.progress.setVisibility(View.INVISIBLE);
        mMainBinding.empty.setVisibility(View.INVISIBLE);
    }
}









