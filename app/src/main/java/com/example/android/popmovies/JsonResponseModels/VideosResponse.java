package com.example.android.popmovies.JsonResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosResponse implements Parcelable
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<VideosModel> results = null;
    public final static Parcelable.Creator<VideosResponse> CREATOR = new Creator<VideosResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VideosResponse createFromParcel(Parcel in) {
            return new VideosResponse(in);
        }

        public VideosResponse[] newArray(int size) {
            return (new VideosResponse[size]);
        }

    }
            ;

    private VideosResponse(Parcel in) {
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.results, (VideosModel.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public VideosResponse() {
    }

    /**
     *
     * @param id the unique movie id number
     * @param results the video (trailer) data
     */
    public VideosResponse(int id, List<VideosModel> results) {
        super();
        this.id = id;
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VideosResponse withId(int id) {
        this.id = id;
        return this;
    }

    public List<VideosModel> getResults() {
        return results;
    }

    public void setResults(List<VideosModel> results) {
        this.results = results;
    }

    public VideosResponse withResults(List<VideosModel> results) {
        this.results = results;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

}
