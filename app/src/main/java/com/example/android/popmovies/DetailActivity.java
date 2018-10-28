package com.example.android.popmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.android.popmovies.Data.MovieUrlConstants;
import com.example.android.popmovies.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements TrailerRecyclerViewAdapter.TrailerItemClickListener, ReviewRecyclerViewAdapter.ReviewItemClickListener {

    private static final String LOG_TAG = DetailActivity.class.getName();

    public static final String CURRENT_MOVIE = "current_movie";

    private TrailerRecyclerViewAdapter mTrailerRecyclerAdapter;

    private ReviewRecyclerViewAdapter mReviewRecyclerAdapter;

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
            Movies currentMovie = intent.getParcelableExtra(CURRENT_MOVIE);

            mDetailBinding.primaryMovieDetails.tvOriginalTitle.setText(currentMovie.original_title);
            mDetailBinding.primaryMovieDetails.tvPlotSynopsis.setText(currentMovie.plot_synopsis);

            mDetailBinding.primaryMovieDetails.tvUserRating.setText(formatUserRatings(currentMovie.user_rating));
            mDetailBinding.primaryMovieDetails.tvReleaseDate.setText(getReleaseDateYear(currentMovie.release_date));

            String fullPosterPath = MovieUrlConstants.BASE_POSTER_URL + MovieUrlConstants.DEFAULT_POSTER_SIZE + currentMovie.poster_path;
            Picasso.with(this).load(fullPosterPath).into(mDetailBinding.primaryMovieDetails.ivMoviePosterDetail);

            String fullBackdropPath = MovieUrlConstants.BASE_POSTER_URL + MovieUrlConstants.DEFAULT_BACKDROP_SIZE + currentMovie.backdrop_path;
            Picasso.with(this).load(fullBackdropPath).into(mDetailBinding.ivMovieBackgroundDetail);

            if (currentMovie.title != null) {
                setTitle(currentMovie.title);
            } else {
                setTitle(R.string.app_name);
            }

            List<String> trailerIds = new ArrayList<String>(currentMovie.trailerIds);

            mDetailBinding.movieTrailers.emptyTrailers.setVisibility(View.VISIBLE);

            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            assert cm != null;
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            if (isConnected) {
                // Create two new adapters that take a list of trailers and reviews as input
                showTrailerRecyclerView();
                int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());
                GridLayoutManager mTrailerRecyclerViewLayoutManager = new GridLayoutManager(this, mNoOfColumns, LinearLayoutManager.VERTICAL, false);
                LinearLayoutManager mReviewRecyclerViewLayoutManager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
                mDetailBinding.movieTrailers.rvMovieTrailers.setLayoutManager(mTrailerRecyclerViewLayoutManager);
                mDetailBinding.movieTrailers.rvMovieReviews.setLayoutManager(mReviewRecyclerViewLayoutManager);
                mDetailBinding.movieTrailers.rvMovieTrailers.setHasFixedSize(true);
                mDetailBinding.movieTrailers.rvMovieReviews.setHasFixedSize(true);
                mTrailerRecyclerAdapter = new TrailerRecyclerViewAdapter(this, trailerIds);
                mTrailerRecyclerAdapter.setClickListener(this);
                mReviewRecyclerAdapter = new ReviewRecyclerViewAdapter(this, currentMovie.reviewAuthor, currentMovie.reviewContent, currentMovie.reviewUrls);
                mReviewRecyclerAdapter.setClickListener(this);
                mDetailBinding.movieTrailers.rvMovieTrailers.setAdapter(mTrailerRecyclerAdapter);
                mDetailBinding.movieTrailers.rvMovieReviews.setAdapter(mReviewRecyclerAdapter);


            } else {
                // Set recycler view visibility to gone
                showEmptyState();
                // Set empty state text to display "No internet connection."
                mDetailBinding.movieTrailers.emptyTrailers.setText(R.string.no_internet);

            }


        }
    }



    @Override
    public void onReviewItemClick(int position) {
        String currentReviewUrl = mReviewRecyclerAdapter.getItem(position);
        if (currentReviewUrl != null) {
            launchWebsite( this, currentReviewUrl);
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
    private void showEmptyState() {
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

