package com.example.android.popmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popmovies.Adapters.FavoriteMovieRecyclerAdapter;
import com.example.android.popmovies.Adapters.MovieRecyclerViewAdapter;
import com.example.android.popmovies.Data.MovieUrlConstants;
import com.example.android.popmovies.JsonResponseModels.MoviesModel;
import com.example.android.popmovies.JsonResponseModels.MoviesResponse;
import com.example.android.popmovies.RoomDatabase.FavoriteMovieEntry;
import com.example.android.popmovies.Utilities.ApiClient;
import com.example.android.popmovies.Utilities.ApiService;
import com.example.android.popmovies.Utilities.CalcNumOfColumns;
import com.example.android.popmovies.Utilities.CheckPreferences;
import com.example.android.popmovies.Utilities.ConnectedToInternet;
import com.example.android.popmovies.ViewModels.FavoriteMovieViewModel;
import com.example.android.popmovies.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, MovieRecyclerViewAdapter.WebItemClickListener, FavoriteMovieRecyclerAdapter.FavoriteItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    private RecyclerView mRecyclerView;
    /**
     * Adapter for the list of movies
     */
    private MovieRecyclerViewAdapter mWebMoviesRecyclerAdapter;

    private FavoriteMovieRecyclerAdapter mFavoriteMovieRecyclerAdapter;

    private boolean mDisplayFavorites;

    private FavoriteMovieViewModel mFavoriteMovieViewModel;

    private String mSortOrder;

    ActivityMainBinding mMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mRecyclerView = mMainBinding.rvMoviePosterGrid;

        mDisplayFavorites = CheckPreferences.getDisplayFavoritesFromPreferences(this);
        mSortOrder = CheckPreferences.getSortOrderFromPreferences(this);
        mFavoriteMovieViewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel.class);
        mMainBinding.empty.setVisibility(View.VISIBLE);

        mFavoriteMovieRecyclerAdapter = new FavoriteMovieRecyclerAdapter(this, new ArrayList<FavoriteMovieEntry>());

        mWebMoviesRecyclerAdapter = new MovieRecyclerViewAdapter(this, new ArrayList<MoviesModel>());


        int mNoOfColumns = CalcNumOfColumns.calculateNoOfColumns(getApplicationContext());
        GridLayoutManager layoutManager = new GridLayoutManager(this, mNoOfColumns, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        if (mRecyclerView.getAdapter() == null) {
            if (ConnectedToInternet.isConnectedToInternet(this) && !mDisplayFavorites) {
                // Load Movies from Web
                updateUiFromWeb();

            } else if (mDisplayFavorites) {
                // Load Movies from Favorites - we can do this even if not connected to internet
                updateUiFromFavorites();
            } else {
                // Set progress bar and recycler view visibility to gone
                showEmptyState();
                // Set empty state text to display "No internet connection."
                mMainBinding.empty.setText(R.string.no_internet);

            }
        }



    }


    private void setRecyclerAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
        checkEmptyDisplay();
    }

    private void checkEmptyDisplay() {
        if (mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter().getItemCount() > 0) {
            showMovieGridView();
        } else {
            showEmptyState();
        }
    }

    private void observerForFavoritesSetup() {
        mSortOrder = CheckPreferences.getSortOrderFromPreferences(this);
        if (mSortOrder.equals(MovieUrlConstants.SORT_BY_MOST_POPULAR_DEFAULT)) {
            mFavoriteMovieViewModel.getAllMoviesByPopularity().observe(this, new Observer<List<FavoriteMovieEntry>>() {
                @Override
                public void onChanged(@Nullable final List<FavoriteMovieEntry> favoriteMovieEntries) {
                    showMovieGridView();
                    mFavoriteMovieRecyclerAdapter.setMovieData(favoriteMovieEntries);
                }
            });
        } else {
            mFavoriteMovieViewModel.getAllMoviesByHighestRated().observe(this, new Observer<List<FavoriteMovieEntry>>() {
                @Override
                public void onChanged(@Nullable List<FavoriteMovieEntry> favoriteMovieEntries) {
                    showMovieGridView();
                    mFavoriteMovieRecyclerAdapter.setMovieData(favoriteMovieEntries);
                }
            });
        }
       /*
       *TODO Move this code to Detail Activity, search results are the results of a search such as findMovie(id)
       *
       * mFavoriteMovieViewModel.getSearchResults().observe(this,
                new Observer<List<FavoriteMovieEntry>>() {
                    @Override
                    public void onChanged(@Nullable final List<FavoriteMovieEntry> favoriteMovieEntries) {

                        if (favoriteMovieEntries.size() > 0) {
                            // TODO if at least 1 movie matches the search results get data from first result
                        } else {
                            // TODO let user know that Movie Id was not found in search results
                        }
                    }
                });*/
    }


    private void updateUiFromFavorites() {
        showProgressBar();
        mFavoriteMovieRecyclerAdapter.setClickListener(this);
        setRecyclerAdapter(mFavoriteMovieRecyclerAdapter);
        observerForFavoritesSetup();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPreferences();

    }


    private void launchDetailActivity(MoviesModel currentMovie) {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.CURRENT_MOVIE, currentMovie);
        this.startActivity(intent);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(LOG_TAG, "Preferences updated");
        checkPreferences();
    }

    private void checkPreferences() {
        mDisplayFavorites = CheckPreferences.getDisplayFavoritesFromPreferences(this);

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
        // Clear the previous adapter
        setRecyclerAdapter(null);

        loadMoviesFromWeb();
        Log.i(LOG_TAG, "TEST: loadMoviesFromWeb() executed");

    }

    private void loadMoviesFromWeb() {
        try {
            if (MovieUrlConstants.API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "API Key not found!!! Please obtain API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }


            showProgressBar();

            mWebMoviesRecyclerAdapter.setClickListener(this);
            setRecyclerAdapter(mWebMoviesRecyclerAdapter);
            Retrofit retrofit = ApiClient.getClient();
            ApiService apiService = retrofit.create(ApiService.class);

            String sortPreference = CheckPreferences.getSortOrderFromPreferences(this);
            Call<MoviesResponse> call;
            if (sortPreference.equals(MovieUrlConstants.SORT_BY_MOST_POPULAR_DEFAULT)) {
                call = apiService.getPopularMovies(MovieUrlConstants.API_KEY);
            } else {
                call = apiService.getTopRatedMovies(MovieUrlConstants.API_KEY);
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
                        mWebMoviesRecyclerAdapter.setMovieData(moviesModelList);
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
    public void onWebItemClick(int position) {
        MoviesModel currentMovie = mWebMoviesRecyclerAdapter.getMoviesModelItem(position);
        launchDetailActivity(currentMovie);
    }

    @Override
    public void onFavoriteItemClick(int position) {
        FavoriteMovieEntry currentFavoriteMovie = mFavoriteMovieRecyclerAdapter.getFavoriteMovieEntryItem(position);

        convertFavoriteAndLaunchDetailActivity(currentFavoriteMovie);
    }

    private void convertFavoriteAndLaunchDetailActivity(FavoriteMovieEntry currentFavoriteMovie) {
        MoviesModel currentMovie = new MoviesModel();
        currentMovie.setId(Integer.parseInt(currentFavoriteMovie.getMovieId()));
        currentMovie.setTitle(currentFavoriteMovie.getTitle());
        currentMovie.setOriginalTitle(currentFavoriteMovie.getOriginalTitle());
        currentMovie.setPosterPath(currentFavoriteMovie.getPosterPath());
        currentMovie.setPopularity(Float.parseFloat(currentFavoriteMovie.getPopularity()));
        currentMovie.setBackdropPath(currentFavoriteMovie.getBackdropPath());
        currentMovie.setVoteAverage(Float.parseFloat(currentFavoriteMovie.getUserRating()));
        currentMovie.setReleaseDate(currentFavoriteMovie.getReleaseDate());
        launchDetailActivity(currentMovie);
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









