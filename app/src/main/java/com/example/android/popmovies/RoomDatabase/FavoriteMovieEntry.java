package com.example.android.popmovies.RoomDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.android.popmovies.Data.MovieContract;


@Entity(tableName = MovieContract.MovieEntry.TABLE_NAME)
public class FavoriteMovieEntry {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_MOVIE_ID)
    private String movieId;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_TITLE)
    private String title;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)
    private String originalTitle;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_POPULARITY)
    private String popularity;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_POSTER_PATH)
    private String posterPath;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_POSTER, typeAffinity = ColumnInfo.BLOB)
    private byte[] poster;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS)
    private String synopsis;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_USER_RATING)
    private String userRating;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_RELEASE_DATE)
    private String releaseDate;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_BACKDROP_PATH)
    private String backdropPath;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_BACKDROP, typeAffinity = ColumnInfo.BLOB)
    private byte[] backdrop;


    public FavoriteMovieEntry(@NonNull String movieId, String title, String originalTitle, String popularity, String posterPath, byte[] poster, String synopsis, String userRating, String releaseDate, String backdropPath, byte[] backdrop) {
        this.movieId = movieId;
        this.title = title;
        this.originalTitle = originalTitle;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.poster = poster;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.backdrop = backdrop;
    }

    @NonNull
    public String getMovieId() {
        return this.movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return this.originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPopularity() {
        return this.popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return this.posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public byte[] getPoster() {
        return this.poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getUserRating() {
        return this.userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBackdropPath() {
        return this.backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public byte[] getBackdrop() {
        return this.backdrop;
    }

    public void setBackdrop(byte[] backdrop) {
        this.backdrop = backdrop;
    }

}