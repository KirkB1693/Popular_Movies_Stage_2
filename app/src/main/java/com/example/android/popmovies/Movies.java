package com.example.android.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Movies implements Parcelable {
    final String title;
    final String original_title;
    final String poster_path; // path to be appended to base url and poster size
    final String plot_synopsis;
    final String user_rating;
    final String release_date;
    final String id;
    final String backdrop_path;
    final List<String> trailerIds;
    final List<String> reviewAuthor;
    final List<String> reviewContent;
    final List<String> reviewUrls;

    public Movies(String mTitle, String mOriginalTitle, String imagePath, String plot, String ratings, String mReleaseDate,
                  String movieId, String backdrop_path, List<String> trailerIds, List<String> reviewAuthor, List<String>
                          reviewContent, List<String> reviewUrls)
    {
        this.title = mTitle;
        this.original_title = mOriginalTitle;
        this.poster_path = imagePath;
        this.plot_synopsis = plot;
        this.user_rating = ratings;
        this.release_date = mReleaseDate;
        this.id = movieId;
        this.backdrop_path = backdrop_path;
        this.trailerIds = trailerIds;
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
        this.reviewUrls = reviewUrls;
    }

    private Movies(Parcel in){
        title = in.readString();
        original_title = in.readString();
        poster_path = in.readString();
        plot_synopsis = in.readString();
        user_rating = in.readString();
        release_date = in.readString();
        id = in.readString();
        backdrop_path = in.readString();
        trailerIds = in.createStringArrayList();
        reviewAuthor = in.createStringArrayList();
        reviewContent = in.createStringArrayList();
        reviewUrls = in.createStringArrayList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return "Title: " + title + "\nOriginal Title: " + original_title + "\nPoster Path: " + poster_path + "\nPlot Synopsis: " + plot_synopsis +
            "\nUser Rating: " + user_rating + "\nRelease Date: " + release_date + "\nMovie ID: " + id + "\nPoster Path: " + poster_path + "\nTrailer Ids: " + trailerIds.toString()
            + "\nReview Author: " + reviewAuthor.toString() + "\nReview Content: " + reviewContent.toString() + "\nReview URLs: " + reviewUrls.toString(); }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(original_title);
        parcel.writeString(poster_path);
        parcel.writeString(plot_synopsis);
        parcel.writeString(user_rating);
        parcel.writeString(release_date);
        parcel.writeString(id);
        parcel.writeString(backdrop_path);
        parcel.writeStringList(trailerIds);
        parcel.writeStringList(reviewAuthor);
        parcel.writeStringList(reviewContent);
        parcel.writeStringList(reviewUrls);
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel parcel) {
            return new Movies(parcel);
        }

        @Override
        public Movies[] newArray(int i) {
            return new Movies[i];
        }

    };
}
