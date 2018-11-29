package com.example.android.popmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popmovies.Adapters.ReviewRecyclerViewAdapter;
import com.example.android.popmovies.Adapters.TrailerRecyclerViewAdapter;
import com.example.android.popmovies.Data.MovieUrlConstants;
import com.example.android.popmovies.JsonResponseModels.MoviesModel;
import com.example.android.popmovies.JsonResponseModels.ReviewsModel;
import com.example.android.popmovies.JsonResponseModels.ReviewsResponse;
import com.example.android.popmovies.JsonResponseModels.VideosModel;
import com.example.android.popmovies.JsonResponseModels.VideosResponse;
import com.example.android.popmovies.RoomDatabase.FavoriteMovieEntry;
import com.example.android.popmovies.Utilities.ApiClient;
import com.example.android.popmovies.Utilities.ApiService;
import com.example.android.popmovies.Utilities.GridUtils;
import com.example.android.popmovies.Utilities.CheckPreferences;
import com.example.android.popmovies.Utilities.ConnectedToInternet;
import com.example.android.popmovies.ViewModels.FavoriteMovieViewModel;
import com.example.android.popmovies.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity implements TrailerRecyclerViewAdapter.TrailerItemClickListener, ReviewRecyclerViewAdapter.ReviewItemClickListener {

    private static final String LOG_TAG = DetailActivity.class.getName();

    public static final String CURRENT_MOVIE = "current_movie_model";

    private TrailerRecyclerViewAdapter mTrailerRecyclerAdapter;

    private ReviewRecyclerViewAdapter mReviewRecyclerAdapter;

    private List<VideosModel> mTrailerModel;

    private List<ReviewsModel> mReviewsModel;

    private FavoriteMovieEntry mMovieEntry;

    private MoviesModel mCurrentMovie;

    private FavoriteMovieViewModel mFavoriteMovieViewModel;

    private boolean mFavorite;

    private ActivityDetailBinding mDetailBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mFavoriteMovieViewModel = new FavoriteMovieViewModel(getApplication());
        android.support.v7.widget.Toolbar toolbar = mDetailBinding.toolbarDetail;
        setSupportActionBar(toolbar);
        mFavoriteMovieViewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        setupUI();

        mDetailBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteFABClicked();
            }
        });
    }


    private void setupUI() {


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        } else {
            mCurrentMovie = intent.getParcelableExtra(CURRENT_MOVIE);
            checkSharedPreferencesForFavoriteStatus();
            FavoriteMovieEntry entry = mMovieEntry;
            observerForFavoritesSetup();
            mFavoriteMovieViewModel.findMovie(Integer.toString(mCurrentMovie.getId()));
            if (mFavorite) {
                populateUiFromFavorite(mCurrentMovie);

            } else {
                populateUiFromIntent(mCurrentMovie);
            }


        }
    }

    private void populateUiFromFavorite(MoviesModel currentMovie) {
        mDetailBinding.primaryMovieDetails.tvOriginalTitle.setText(currentMovie.getOriginalTitle());
        mDetailBinding.primaryMovieDetails.tvPlotSynopsis.setText(currentMovie.getOverview());

        mDetailBinding.primaryMovieDetails.ratingBar.setRating(currentMovie.getVoteAverage());
        mDetailBinding.primaryMovieDetails.tvReleaseDate.setText(getReleaseDateYear(currentMovie.getReleaseDate()));


        if (currentMovie.getTitle() != null) {
            setTitle(currentMovie.getTitle());
        } else {
            setTitle(R.string.app_name);
        }


        if (ConnectedToInternet.isConnectedToInternet(this)) {
            loadTrailersFromWeb(currentMovie.getId());
            loadReviewsFromWeb(currentMovie.getId());
        } else {
            mDetailBinding.movieTrailers.emptyTrailers.setVisibility(View.VISIBLE);
            mDetailBinding.movieTrailers.emptyTrailers.setText(getString(R.string.no_internet));
        }


    }

    private void observerForFavoritesSetup() {
        mFavoriteMovieViewModel.getSearchResults().observe(this,
                new Observer<List<FavoriteMovieEntry>>() {
                    @Override
                    public void onChanged(@Nullable final List<FavoriteMovieEntry> favoriteMovieEntries) {
                        if (favoriteMovieEntries != null) {
                            if (favoriteMovieEntries.size() > 0) {
                                mFavorite = true;
                                changeFAB();
                                mMovieEntry = favoriteMovieEntries.get(0);
                                mDetailBinding.primaryMovieDetails.ivMoviePosterDetail.setImageBitmap(BitmapFactory.decodeByteArray(mMovieEntry.getPoster(), 0, mMovieEntry.getPoster().length));
                                mDetailBinding.expandedImage.setImageBitmap(BitmapFactory.decodeByteArray(mMovieEntry.getBackdrop(), 0, mMovieEntry.getBackdrop().length));
                            }
                        } else {
                            if (mFavorite) {
                                Toast.makeText(getApplicationContext(), "No Match to MovieId in Search Results", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void populateUiFromIntent(MoviesModel currentMovie) {
        mDetailBinding.primaryMovieDetails.tvOriginalTitle.setText(currentMovie.getOriginalTitle());
        mDetailBinding.primaryMovieDetails.tvPlotSynopsis.setText(currentMovie.getOverview());

        mDetailBinding.primaryMovieDetails.ratingBar.setRating(currentMovie.getVoteAverage());
        mDetailBinding.primaryMovieDetails.tvReleaseDate.setText(getReleaseDateYear(currentMovie.getReleaseDate()));

        String fullPosterPath = MovieUrlConstants.BASE_POSTER_URL + MovieUrlConstants.DEFAULT_POSTER_SIZE + currentMovie.getPosterPath();
        Picasso.with(this).load(fullPosterPath).into(mDetailBinding.primaryMovieDetails.ivMoviePosterDetail);

        String fullBackdropPath = MovieUrlConstants.BASE_POSTER_URL + MovieUrlConstants.DEFAULT_BACKDROP_SIZE + currentMovie.getBackdropPath();
        Picasso.with(this).load(fullBackdropPath).into(mDetailBinding.expandedImage);

        if (currentMovie.getTitle() != null) {
            setTitle(currentMovie.getTitle());
        } else {
            setTitle(R.string.app_name);
        }


        if (ConnectedToInternet.isConnectedToInternet(this)) {
            loadTrailersFromWeb(currentMovie.getId());
            loadReviewsFromWeb(currentMovie.getId());
        } else {
            mDetailBinding.movieTrailers.emptyTrailers.setVisibility(View.VISIBLE);
            mDetailBinding.movieTrailers.emptyTrailers.setText(getString(R.string.no_internet));
        }
    }

    private void showTrailers() {
        if (ConnectedToInternet.isConnectedToInternet(this)) {
            // Set empty state text to display "No trailers found."
            if (mTrailerModel == null || mTrailerModel.isEmpty()) {
                showTrailersEmptyState();
                mDetailBinding.movieTrailers.emptyTrailers.setText(R.string.error_no_trailers_found);
            }
            // If there is a valid list of {@link VideosModel}s, then add them to the adapter's
            // data set. This will trigger the RecyclerView Grid to update.
            if (mTrailerModel != null && !mTrailerModel.isEmpty()) {
                showTrailerRecyclerView();
                int mNoOfColumns = GridUtils.calculateNoOfColumns(getApplicationContext());
                GridLayoutManager mTrailerRecyclerViewLayoutManager = new GridLayoutManager(this, mNoOfColumns, LinearLayoutManager.VERTICAL, false);
                mDetailBinding.movieTrailers.rvMovieTrailers.setLayoutManager(mTrailerRecyclerViewLayoutManager);
                mDetailBinding.movieTrailers.rvMovieTrailers.setHasFixedSize(true);
                mTrailerRecyclerAdapter = new TrailerRecyclerViewAdapter(this, mTrailerModel);
                mTrailerRecyclerAdapter.setClickListener(this);
                mDetailBinding.movieTrailers.rvMovieTrailers.setAdapter(mTrailerRecyclerAdapter);
            }


        } else {
            // Set recycler view visibility to gone
            showTrailersEmptyState();
            // Set empty state text to display "No internet connection."
            mDetailBinding.movieTrailers.emptyTrailers.setText(R.string.no_internet);

        }
    }


    private void showReviews() {
        if (ConnectedToInternet.isConnectedToInternet(this)) {
            if (mReviewsModel != null && !mReviewsModel.isEmpty()) {
                LinearLayoutManager mReviewRecyclerViewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                mDetailBinding.movieTrailers.rvMovieReviews.setLayoutManager(mReviewRecyclerViewLayoutManager);
                mDetailBinding.movieTrailers.rvMovieReviews.setHasFixedSize(true);
                mReviewRecyclerAdapter = new ReviewRecyclerViewAdapter(this, mReviewsModel);
                mReviewRecyclerAdapter.setClickListener(this);
                mDetailBinding.movieTrailers.rvMovieReviews.setAdapter(mReviewRecyclerAdapter);
            }
        }
    }

    private void loadTrailersFromWeb(int currentMovieId) {
        try {
            Retrofit retrofit = ApiClient.getClient();
            ApiService apiService = retrofit.create(ApiService.class);

            Call<VideosResponse> call = apiService.getMovieTrailer(currentMovieId, MovieUrlConstants.API_KEY);

            call.enqueue(new Callback<VideosResponse>() {
                @Override
                public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                    mTrailerModel = response.body().getResults();
                    showTrailers();
                }

                @Override
                public void onFailure(Call<VideosResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error Fetching Trailer Data!", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void loadReviewsFromWeb(int currentMovieId) {
        try {
            Retrofit retrofit = ApiClient.getClient();
            ApiService apiService = retrofit.create(ApiService.class);

            Call<ReviewsResponse> call = apiService.getReviews(currentMovieId, MovieUrlConstants.API_KEY);

            call.enqueue(new Callback<ReviewsResponse>() {
                @Override
                public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                    mReviewsModel = response.body().getResults();
                    showReviews();
                }

                @Override
                public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error Fetching Review Data!", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onFavoriteFABClicked() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mFavorite = !mFavorite;
        // if not currently a favorite, add movie to favorite movies db, change FAB and update preferences to show favorites
        if (mFavorite) {
            if (mMovieEntry == null) {
                Bitmap posterBitmap = ((BitmapDrawable) mDetailBinding.primaryMovieDetails.ivMoviePosterDetail.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                posterBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] posterInBytes = baos.toByteArray();
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap backdropBitmap = ((BitmapDrawable) mDetailBinding.expandedImage.getDrawable()).getBitmap();
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                backdropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
                byte[] backdropInBytes = baos2.toByteArray();
                try {
                    baos2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMovieEntry = new FavoriteMovieEntry(Integer.toString(mCurrentMovie.getId()), mCurrentMovie.getTitle(), mCurrentMovie.getOriginalTitle(),
                        Float.toString(mCurrentMovie.getPopularity()),
                        mCurrentMovie.getPosterPath(), posterInBytes,
                        mCurrentMovie.getOverview(),
                        Float.toString(mCurrentMovie.getVoteAverage()),
                        mCurrentMovie.getReleaseDate(),
                        mCurrentMovie.getBackdropPath(),
                        backdropInBytes);
            }

            mFavoriteMovieViewModel.insertMovie(mMovieEntry);

            changeFAB();


            Toast.makeText(this, mDetailBinding.primaryMovieDetails.tvOriginalTitle.getText() + " added to Favorites", Toast.LENGTH_SHORT).show();
        } else {
            // if unchecked, deleted movie from favorite movies db and update preferences to show all movies
            mFavoriteMovieViewModel.deleteMovie(mMovieEntry.getMovieId());

            changeFAB();


            Toast.makeText(this, mDetailBinding.primaryMovieDetails.tvOriginalTitle.getText() + " removed from Favorites", Toast.LENGTH_SHORT).show();
        }

    }

    private void changeFAB() {
        if (mFavorite) {
            mDetailBinding.fab.setImageResource(R.drawable.glossy_heart);
        } else {
            mDetailBinding.fab.setImageResource(R.drawable.glossy_heart_gray);
        }
    }

    private void checkSharedPreferencesForFavoriteStatus() {
        // check if item is a favorite (shared preference is favorite) and set the checkbox status appropriately
        mFavorite = CheckPreferences.getDisplayFavoritesFromPreferences(this);
        changeFAB();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onReviewItemClick(int position) {
        String currentReviewUrl = mReviewRecyclerAdapter.getItem(position);
        if (currentReviewUrl != null) {
            launchWebsite(currentReviewUrl);
        }
    }

    private void launchWebsite(String currentReviewUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(currentReviewUrl));
        startActivity(intent);
    }

    @Override
    public void onTrailerItemClick(int position) {
        String currentTrailer = mTrailerRecyclerAdapter.getItem(position);
        if (currentTrailer != null) {
            launchVideoActivity(this, currentTrailer);
        }
    }

    private void launchVideoActivity(Context context, String currentTrailerId) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MovieUrlConstants.BASE_YOUTUBE_APP + currentTrailerId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(MovieUrlConstants.BASE_YOUTUBE_URL + currentTrailerId));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    private void showTrailersEmptyState() {
        mDetailBinding.movieTrailers.rvMovieTrailers.setVisibility(View.INVISIBLE);
        mDetailBinding.movieTrailers.emptyTrailers.setVisibility(View.VISIBLE);
    }


    private void showTrailerRecyclerView() {
        mDetailBinding.movieTrailers.rvMovieTrailers.setVisibility(View.VISIBLE);
        mDetailBinding.movieTrailers.emptyTrailers.setVisibility(View.INVISIBLE);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.close_on_error, Toast.LENGTH_SHORT).show();
    }

    private String getReleaseDateYear(String releaseDate) {
        String[] date = releaseDate.split(getString(R.string.date_separator_string));
        return date[0];
    }

}

