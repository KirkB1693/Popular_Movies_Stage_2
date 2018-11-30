package com.example.android.popmovies.JsonResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse implements Parcelable
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<ReviewsModel> results = null;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    public final static Parcelable.Creator<ReviewsResponse> CREATOR = new Creator<ReviewsResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ReviewsResponse createFromParcel(Parcel in) {
            return new ReviewsResponse(in);
        }

        public ReviewsResponse[] newArray(int size) {
            return (new ReviewsResponse[size]);
        }

    }
            ;

    private ReviewsResponse(Parcel in) {
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.page = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.results, (ReviewsModel.class.getClassLoader()));
        this.totalPages = ((int) in.readValue((int.class.getClassLoader())));
        this.totalResults = ((int) in.readValue((int.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public ReviewsResponse() {
    }

    /**
     *
     * @param id the unique movie id number
     * @param results the reviews for this movie
     * @param totalResults the total number of reviews available
     * @param page the page of review data
     * @param totalPages the total number of pages of review data available
     */
    public ReviewsResponse(int id, int page, List<ReviewsModel> results, int totalPages, int totalResults) {
        super();
        this.id = id;
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ReviewsResponse withId(int id) {
        this.id = id;
        return this;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ReviewsResponse withPage(int page) {
        this.page = page;
        return this;
    }

    public List<ReviewsModel> getResults() {
        return results;
    }

    public void setResults(List<ReviewsModel> results) {
        this.results = results;
    }

    public ReviewsResponse withResults(List<ReviewsModel> results) {
        this.results = results;
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public ReviewsResponse withTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ReviewsResponse withTotalResults(int totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(page);
        dest.writeList(results);
        dest.writeValue(totalPages);
        dest.writeValue(totalResults);
    }

    public int describeContents() {
        return 0;
    }

}
