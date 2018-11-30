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
    Call<MoviesResponse> getPopularMovies(@Query(MovieUrlConstants.API_PARAM) String apiKey, @Query(MovieUrlConstants.PAGE_URL_PARAM) int page, @Query(MovieUrlConstants.LANGUAGE_PARAM) String language);

    @GET(MovieUrlConstants.SORT_BY_HIGHEST_RATED)
    Call<MoviesResponse> getTopRatedMovies(@Query(MovieUrlConstants.API_PARAM) String apiKey, @Query(MovieUrlConstants.PAGE_URL_PARAM) int page, @Query(MovieUrlConstants.LANGUAGE_PARAM) String language);

    //Trailers
    @GET("{movie_id}/" + MovieUrlConstants.TRAILERS_URL_ADDITIONAL_PATH)
    Call<VideosResponse> getMovieTrailer(@Path(MovieUrlConstants.MOVIE_ID_PATH) int id, @Query(MovieUrlConstants.API_PARAM) String apiKey);

    //ReviewsModel
    @GET("{movie_id}/" + MovieUrlConstants.REVIEWS_URL_ADDITIONAL_PATH)
    Call<ReviewsResponse> getReviews(@Path(MovieUrlConstants.MOVIE_ID_PATH) int id, @Query(MovieUrlConstants.API_PARAM) String apiKey);

}
