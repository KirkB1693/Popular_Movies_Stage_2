package com.example.android.popmovies.JsonResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MoviesResponse implements Parcelable {

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("results")
    @Expose
    private List<MoviesModel> results = null;


    public final static Parcelable.Creator<MoviesResponse> CREATOR = new Creator<MoviesResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MoviesResponse createFromParcel(Parcel in) {
            return new MoviesResponse(in);
        }

        public MoviesResponse[] newArray(int size) {
            return (new MoviesResponse[size]);
        }

    };

    private MoviesResponse(Parcel in) {
        this.page = ((int) in.readValue((int.class.getClassLoader())));
        this.totalResults = ((int) in.readValue((int.class.getClassLoader())));
        this.totalPages = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.results, (MoviesModel.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     */
    public MoviesResponse() {
    }

    /**
     * @param results the individual movie data
     * @param totalResults the total number of movie results available
     * @param page the page of the movie data provided
     * @param totalPages the total pages of data available
     */
    public MoviesResponse(int page, int totalResults, int totalPages, List<MoviesModel> results) {
        super();
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<MoviesModel> getResults() {
        return results;
    }

    public void setResults(List<MoviesModel> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeValue(totalResults);
        dest.writeValue(totalPages);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

}

