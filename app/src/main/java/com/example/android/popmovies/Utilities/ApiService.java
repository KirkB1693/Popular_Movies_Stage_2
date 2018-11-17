package com.example.android.popmovies.Utilities;

import com.example.android.popmovies.Data.MovieUrlConstants;
import com.example.android.popmovies.JsonResponseModels.MoviesResponse;
import com.example.android.popmovies.JsonResponseModels.ReviewsResponse;
import com.example.android.popmovies.JsonResponseModels.VideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET(MovieUrlConstants.SORT_BY_MOST_POPULAR_DEFAULT)
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET(MovieUrlConstants.SORT_BY_HIGHEST_RATED)
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    //Trailers
    @GET("{movie_id}/" + MovieUrlConstants.TRAILERS_URL_ADDITIONAL_PATH)
    Call<VideosResponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);

    //ReviewsModel
    @GET("{movie_id}/" + MovieUrlConstants.REVIEWS_URL_ADDITIONAL_PATH)
    Call<ReviewsResponse> getReviews(@Path("movie_id") int id, @Query("api_key") String apiKey);

}
