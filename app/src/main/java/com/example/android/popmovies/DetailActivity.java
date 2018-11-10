package com.example.android.popmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
import com.example.android.popmovies.Utilities.ApiClient;
import com.example.android.popmovies.Utilities.ApiService;
import com.example.android.popmovies.Utilities.CalcNumOfColumns;
import com.example.android.popmovies.Utilities.ConnectedToInternet;
import com.example.android.popmovies.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

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

    ActivityDetailBinding mDetailBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        setupUI();
    }


    private void setupUI() {


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        } else {
            MoviesModel currentMovie = intent.getParcelableExtra(CURRENT_MOVIE);

            mDetailBinding.primaryMovieDetails.tvOriginalTitle.setText(currentMovie.getOriginalTitle());
            mDetailBinding.primaryMovieDetails.tvPlotSynopsis.setText(currentMovie.getOverview());

            mDetailBinding.primaryMovieDetails.tvUserRating.setText(formatUserRatings(Float.toString(currentMovie.getVoteAverage())));
            mDetailBinding.primaryMovieDetails.tvReleaseDate.setText(getReleaseDateYear(currentMovie.getReleaseDate()));

            String fullPosterPath = MovieUrlConstants.BASE_POSTER_URL + MovieUrlConstants.DEFAULT_POSTER_SIZE + currentMovie.getPosterPath();
            Picasso.with(this).load(fullPosterPath).into(mDetailBinding.primaryMovieDetails.ivMoviePosterDetail);

            String fullBackdropPath = MovieUrlConstants.BASE_POSTER_URL + MovieUrlConstants.DEFAULT_BACKDROP_SIZE + currentMovie.getBackdropPath();
            Picasso.with(this).load(fullBackdropPath).into(mDetailBinding.ivMovieBackgroundDetail);

            if (currentMovie.getTitle() != null) {
                setTitle(currentMovie.getTitle());
            } else {
                setTitle(R.string.app_name);
            }

            if (ConnectedToInternet.isConnectedToInternet(this)) {
                loadTrailersFromWeb(currentMovie.getId());
                loadReviewsFromWeb(currentMovie.getId());
            }


            mDetailBinding.movieTrailers.emptyTrailers.setVisibility(View.VISIBLE);


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
                int mNoOfColumns = CalcNumOfColumns.calculateNoOfColumns(getApplicationContext());
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
            if (BuildConfig.API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "API Key not found!!! Please obtain API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }

            Retrofit retrofit = ApiClient.getClient();
            ApiService apiService = retrofit.create(ApiService.class);

            Call<VideosResponse> call = apiService.getMovieTrailer(currentMovieId, BuildConfig.API_KEY);

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
            if (BuildConfig.API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "API Key not found!!! Please obtain API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }

            Retrofit retrofit = ApiClient.getClient();
            ApiService apiService = retrofit.create(ApiService.class);

            Call<ReviewsResponse> call = apiService.getReviews(currentMovieId, BuildConfig.API_KEY);

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


    @Override
    public void onReviewItemClick(int position) {
        String currentReviewUrl = mReviewRecyclerAdapter.getItem(position);
        if (currentReviewUrl != null) {
            launchWebsite(this, currentReviewUrl);
        }
    }

    private void launchWebsite(DetailActivity detailActivity, String currentReviewUrl) {
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
        String[] date = releaseDate.split(getString(R.string.date_seperator_string));
        return date[0];
    }

    private String formatUserRatings(String rating) {
        return (rating + getString(R.string.end_format_for_rating));
    }
}

