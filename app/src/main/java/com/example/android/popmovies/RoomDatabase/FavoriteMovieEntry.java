package com.example.android.popmovies.RoomDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.android.popmovies.Data.MovieContract;

import java.util.List;


@Entity(tableName = MovieContract.MovieEntry.TABLE_NAME)
public class FavoriteMovieEntry {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_MOVIE_ID)
    private String movieId;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_TITLE)
    private String title;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)
    private String originalTitle;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_POPULARITY)
    private String popularity;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_POSTER, typeAffinity = ColumnInfo.BLOB)
    private byte[] poster;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS)
    private String synopsis;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_USER_RATING)
    private String userRating;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_RELEASE_DATE)
    private String releaseDate;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_BACKDROP, typeAffinity = ColumnInfo.BLOB)
    private byte[] backdrop;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_TRAILER_IDS)
    private List<String> trailerIds;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_REVIEW_AUTHORS)
    private List<String> reviewAuthors;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_REVIEW_CONTENTS)
    private List<String> reviewContents;

    @ColumnInfo(name = MovieContract.MovieEntry.COLUMN_REVIEW_URLS)
    private List<String> reviewUrls;



    public FavoriteMovieEntry(String movieId, String title, String originalTitle, String popularity, byte[] poster, String synopsis, String userRating, String releaseDate, byte[] backdrop,
                              List<String> trailerIds, List<String> reviewAuthors, List<String> reviewContents, List<String> reviewUrls) {
        this.movieId = movieId;
        this.title = title;
        this.originalTitle = originalTitle;
        this.popularity = popularity;
        this.poster = poster;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.backdrop = backdrop;
        this.trailerIds = trailerIds;
        this.reviewAuthors = reviewAuthors;
        this.reviewContents = reviewContents;
        this.reviewUrls = reviewUrls;
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

    public byte[] getBackdrop() {
        return this.backdrop;
    }

    public void setBackdrop(byte[] backdrop) {
        this.backdrop = backdrop;
    }

    public List<String> getTrailerIds() {
        return this.trailerIds;
    }

    public void setTrailerIds(List<String> trailerIds) {
        this.trailerIds = trailerIds;
    }

    public List<String> getReviewAuthors() {
        return this.reviewAuthors;
    }

    public void setReviewAuthors(List<String> reviewAuthors) {
        this.reviewAuthors = reviewAuthors;
    }

    public List<String> getReviewContents() {
        return this.reviewContents;
    }

    public void setReviewContents(List<String> reviewContents) {
        this.reviewContents = reviewContents;
    }

    public List<String> getReviewUrls() {
        return this.reviewUrls;
    }

    public void setReviewUrls(List<String> reviewUrls) {
        this.reviewUrls = reviewUrls;
    }

}