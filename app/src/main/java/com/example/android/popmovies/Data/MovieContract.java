package com.example.android.popmovies.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popmovies";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for PopMovies.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /*
     * Possible paths that can be appended to BASE_CONTENT_URI to form valid URI's that PopMovies
     * can handle. For instance,
     *
     *     content://com.example.android.popmovies/movies/
     *     [           BASE_CONTENT_URI         ][ PATH_MOVIES ]
     *
     * is a valid path for looking at movie data.
     *
     *      content://com.example.android.popmovies/givemeroot/
     *
     * will fail, as the ContentProvider hasn't been given any information on what to do with
     * "givemeroot".
     */
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";

    /* Inner class that defines the table contents of the movies table */
    public static final class MovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the MoviesModel table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIES)
                .build();

        /* Used internally as the name of our movies table. */
        public static final String TABLE_NAME = "favorite_movies";


        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        /* Movie ID as returned by API, used to identify the movie */
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_POPULARITY = "popularity";

        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_PLOT_SYNOPSIS = "synopsis";

        public static final String COLUMN_USER_RATING = "user_rating";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_BACKDROP = "backdrop";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        /**
         * Builds a URI that adds the movie id to the end of the movie content URI path.
         * This is used to query details about a single movie entry by id. This is what we
         * use for the detail view query. We assume a valid movie id is passed to this method.
         *
         * @param movieId The movie Id as returned by the API
         * @return Uri to query details about a single movie entry
         */
        public static Uri buildMovieUriWithMovieId(String movieId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(movieId)
                    .build();
        }


    }
}
