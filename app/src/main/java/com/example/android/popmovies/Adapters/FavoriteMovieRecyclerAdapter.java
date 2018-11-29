package com.example.android.popmovies.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popmovies.R;
import com.example.android.popmovies.RoomDatabase.FavoriteMovieEntry;
import com.example.android.popmovies.Utilities.GridUtils;

import java.util.List;

public class FavoriteMovieRecyclerAdapter extends RecyclerView.Adapter<FavoriteMovieRecyclerAdapter.ViewHolder> {

    private List<FavoriteMovieEntry> mFavoriteMovieEntries;
    private FavoriteItemClickListener mClickListener;
    private final Context mContext;

    // data is passed into the constructor
    public FavoriteMovieRecyclerAdapter(Context context, List<FavoriteMovieEntry> favoriteMovieEntries) {
        this.mFavoriteMovieEntries = favoriteMovieEntries;
        this.mContext = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        params.height = GridUtils.gridItemHeightInPixelsFromWidth(parent.getContext());
        return new ViewHolder(view);
    }

    // binds the image to the ImageView in each cell
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.itemView.setTag(mFavoriteMovieEntries.get(position));
        FavoriteMovieEntry favoriteMovieEntry = mFavoriteMovieEntries.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(favoriteMovieEntry.getPoster(), 0, favoriteMovieEntry.getPoster().length);
        holder.myImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.myImageView.setImageBitmap(bitmap);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mFavoriteMovieEntries.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onFavoriteItemClick(getAdapterPosition());
        }
    }


    // convenience method for getting data at click position
    public FavoriteMovieEntry getFavoriteMovieEntryItem(int id) {
        return mFavoriteMovieEntries.get(id);
    }

    public void setMovieData(List<FavoriteMovieEntry> favoriteMovieEntries) {
        mFavoriteMovieEntries = favoriteMovieEntries;
        notifyDataSetChanged();
    }

    // allows clicks events to be caught
    public void setClickListener(FavoriteItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface FavoriteItemClickListener {
        void onFavoriteItemClick(int position);
    }

    public void clear() {
        if (mFavoriteMovieEntries != null) {
            final int favoriteSize = mFavoriteMovieEntries.size();
            if (favoriteSize > 0) {
                for (int i = 0; i < favoriteSize; i++) {
                    mFavoriteMovieEntries.remove(0);
                }

                notifyItemRangeRemoved(0, favoriteSize);
            }
        }
    }
}
