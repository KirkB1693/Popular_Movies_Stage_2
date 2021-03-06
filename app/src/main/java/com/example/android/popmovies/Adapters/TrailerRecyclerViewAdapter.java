package com.example.android.popmovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popmovies.Data.MovieUrlConstants;
import com.example.android.popmovies.JsonResponseModels.VideosModel;
import com.example.android.popmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.ViewHolder> {
    private List<VideosModel> mTrailerIds;
    private TrailerRecyclerViewAdapter.TrailerItemClickListener mClickListener;
    private final Context mContext;

    // data is passed into the constructor
    public TrailerRecyclerViewAdapter(Context context, List<VideosModel> trailerIds) {
        this.mTrailerIds = trailerIds;
        this.mContext = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public TrailerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the image to the ImageView in each cell
    @Override
    public void onBindViewHolder(@NonNull TrailerRecyclerViewAdapter.ViewHolder holder, int position) {
        VideosModel trailerId = mTrailerIds.get(position);
        holder.itemView.setTag(trailerId);
        String fullTrailerThumbnailPath = MovieUrlConstants.BASE_YOUTUBE_THUMBNAIL_URL + trailerId.getKey() + MovieUrlConstants.YOUTUBE_THUMBNAIL_OPTION;
        Picasso.with(mContext).load(fullTrailerThumbnailPath).placeholder(R.drawable.ic_movie_loading).into(holder.myImageView);

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mTrailerIds.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myImageView = (ImageView) itemView.findViewById(R.id.iv_movie_trailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onTrailerItemClick(getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mTrailerIds.get(id).getKey();
    }

    void setTrailerData (List<VideosModel> trailerIds){
        mTrailerIds = trailerIds;
        notifyDataSetChanged();
    }

    // allows clicks events to be caught
    public void setClickListener(TrailerRecyclerViewAdapter.TrailerItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface TrailerItemClickListener {
        void onTrailerItemClick(int position);
    }

    public void clear() {
        final int size = mTrailerIds.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mTrailerIds.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}
