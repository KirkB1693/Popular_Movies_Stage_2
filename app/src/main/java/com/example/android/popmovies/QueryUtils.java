package com.example.android.popmovies;

import android.net.Uri;
import android.util.Log;

import com.example.android.popmovies.Data.MovieUrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving news data from The Guardian.
 */
final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Movies} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Movies> extractMovies(String webUrl) {

        Log.i(LOG_TAG, "TEST: extractMovies() has been called");

        // Create an empty ArrayList that we can start adding news to
        ArrayList<Movies> movies = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of News objects with the corresponding data.
            JSONObject baseJsonObject = new JSONObject(fetchMovieData(webUrl));
            JSONArray resultsArray = baseJsonObject.getJSONArray("results");
            if (resultsArray != null && resultsArray.length() > 0) {
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject movieObject = resultsArray.getJSONObject(i);


                    // Set a default title in case there isn't a link in the JSON response
                    String title = "";
                    try {
                        title = movieObject.getString("title");
                    } catch (Exception e) {
                        Log.e("QueryUtils", "Problem getting title in the movie JSON results", e);
                    }

                    // Set a default original title in case there isn't a link in the JSON response
                    String original_title = "";
                    try {
                        original_title = movieObject.getString("original_title");
                    } catch (Exception e) {
                        Log.e("QueryUtils", "Problem getting section in the movie JSON results", e);
                    }


                    // Set a default plot synopsis in case there isn't a link in the JSON response
                    String overview = "";
                    try {
                        // Strip Html formatting codes from the body text, remove nextline and [obj] characters
                        overview = movieObject.getString("overview");
                    } catch (Exception e) {
                        Log.e("QueryUtils", "Problem getting plot synopsis in the movie JSON results", e);
                    }

                    // Set a default poster path in case there isn't a link in the JSON response
                    String poster_path = "";
                    try {
                        poster_path = movieObject.getString("poster_path");

                    } catch (Exception e) {
                        Log.e("QueryUtils", "Problem getting poster path in the movie JSON results", e);
                    }

                    // Set a default release date in case there isn't a link in the JSON response
                    String releaseDate = "";
                    try {
                        releaseDate = movieObject.getString("release_date");
                    } catch (Exception e) {
                        Log.e("QueryUtils", "Problem getting release date in the movie JSON results", e);
                    }

                    // Set a default user rating in case there isn't a link in the JSON response
                    String userRating = "";
                    try {
                        userRating = movieObject.getString("vote_average");
                    } catch (Exception e) {
                        Log.e("QueryUtils", "Problem getting user rating in the movie JSON results", e);
                    }

                    // Set a default movie id in case there isn't a link in the JSON response
                    String id = "";
                    try {
                        id = movieObject.getString("id");
                    } catch (Exception e) {
                        Log.e("QueryUtils", "Problem getting movie id in the movie JSON results", e);
                    }

                    // Set a default movie backdrop_path in case there isn't a link in the JSON response
                    String backdrop_path = "";
                    try {
                        backdrop_path = movieObject.getString("backdrop_path");
                    } catch (Exception e) {
                        Log.e("QueryUtils", "Problem getting backdrop_path in the movie JSON results", e);
                    }

                    ArrayList<String> trailerIds = new ArrayList<String>();
                    try {
                        JSONObject baseTrailerJsonObject = new JSONObject(fetchTrailerData(id));
                        JSONArray trailerResultsArray = baseTrailerJsonObject.getJSONArray("results");
                        if (trailerResultsArray != null && trailerResultsArray.length() > 0) {
                            for (int j = 0; j < trailerResultsArray.length(); j++) {
                                JSONObject trailerObject = trailerResultsArray.getJSONObject(j);
                                trailerIds.add(trailerObject.getString("key"));
                            }
                        }
                    } catch (Exception e) {
                        Log.e("QueryUtils", "Problem getting trailer ids in the trailer JSON results", e);
                    }

                    ArrayList<String> reviewAuthor = new ArrayList<String>();
                    ArrayList<String> reviewContent = new ArrayList<String>();
                    ArrayList<String> reviewUrls = new ArrayList<String>();
                    try {
                        JSONObject baseReviewJsonObject = new JSONObject(fetchReviewData(id));
                        JSONArray reviewResultsArray = baseReviewJsonObject.getJSONArray("results");
                        if (reviewResultsArray != null && reviewResultsArray.length() > 0) {
                            for (int j = 0; j < reviewResultsArray.length(); j++) {
                                JSONObject trailerObject = reviewResultsArray.getJSONObject(j);
                                reviewAuthor.add(trailerObject.getString("author"));
                                reviewContent.add(trailerObject.getString("content"));
                                reviewUrls.add(trailerObject.getString("url"));
                            }
                        }
                    } catch (Exception e) {
                        Log.e("QueryUtils", "Problem getting review data in the review JSON results", e);
                    }

                    movies.add(new Movies(title, original_title, poster_path, overview, userRating, releaseDate, id, backdrop_path, trailerIds, reviewAuthor, reviewContent, reviewUrls));

                }
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of movies
        return movies;
    }

    /**
     * Query the Movie Database API dataset and return a String JSON response to represent a single movie search.
     */
    private static String fetchMovieData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return jsonResponse;
    }

    private static String fetchTrailerData(String movieId) {
        // Create URL object
        Uri baseUri = Uri.parse(MovieUrlConstants.BASE_SEARCH_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendEncodedPath(movieId);
        uriBuilder.appendEncodedPath(MovieUrlConstants.TRAILERS_URL_ADDITIONAL_PATH);
        uriBuilder.appendQueryParameter(MovieUrlConstants.API_PARAM, MovieUrlConstants.API_KEY);
        uriBuilder.appendQueryParameter(MovieUrlConstants.LANGUAGE_PARAM, MovieUrlConstants.DEFAULT_LANGUAGE);

        String requestUrl = uriBuilder.toString();
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return jsonResponse;
    }

    private static String fetchReviewData(String movieId) {
        // Create URL object
        Uri baseUri = Uri.parse(MovieUrlConstants.BASE_SEARCH_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendEncodedPath(movieId);
        uriBuilder.appendEncodedPath(MovieUrlConstants.REVIEWS_URL_ADDITIONAL_PATH);
        uriBuilder.appendQueryParameter(MovieUrlConstants.API_PARAM, MovieUrlConstants.API_KEY);
        uriBuilder.appendQueryParameter(MovieUrlConstants.LANGUAGE_PARAM, MovieUrlConstants.DEFAULT_LANGUAGE);

        String requestUrl = uriBuilder.toString();
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return jsonResponse;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


}
