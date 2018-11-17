package com.example.android.popmovies.Data;

import com.example.android.popmovies.BuildConfig;

public class MovieUrlConstants {

    final public static String API_KEY = BuildConfig.API_KEY;

    final public static String BASE_SEARCH_URL = "http://api.themoviedb.org/3/movie/";

    final public static String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";

    final public static String DEFAULT_POSTER_SIZE = "w185/";

    final public static String DEFAULT_BACKDROP_SIZE = "w500/";

    final public static String LANGUAGE_PARAM = "language";

    final public static String DEFAULT_LANGUAGE = "en-US";

    final public static String API_PARAM = "api_key";

    final public static String SORT_BY_MOST_POPULAR_DEFAULT = "popular";

    final public static String SORT_BY_HIGHEST_RATED = "top_rated";

    final public static String TRAILERS_URL_ADDITIONAL_PATH = "videos";

    final public static String REVIEWS_URL_ADDITIONAL_PATH = "reviews";

    final public static String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    final public static String BASE_YOUTUBE_APP = "vnd.youtube:";

    final public static String BASE_YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/";

    final public static String YOUTUBE_THUMBNAIL_OPTION = "/0.jpg";

}
