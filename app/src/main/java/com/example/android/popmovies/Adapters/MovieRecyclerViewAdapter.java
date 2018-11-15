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

import com.example.android.popmovies.Data.MovieUrlConstants;
import com.example.android.popmovies.JsonResponseModels.MoviesModel;
import com.example.android.popmovies.R;
import com.example.android.popmovies.RoomDatabase.FavoriteMovieEntry;
import com.example.android.popmovies.Utilities.CheckPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private List<MoviesModel> mMovies;
    private List<FavoriteMovieEntry> mFavoriteMovieEntries;
    private ItemClickListener mClickListener;
    private final Context mContext;

    // data is passed into the constructor
    public MovieRecyclerViewAdapter(Context context, List<MoviesModel> movies, List<FavoriteMovieEntry> favoriteMovieEntries) {
        this.mMovies = movies;
        this.mFavoriteMovieEntries = favoriteMovieEntries;
        this.mContext = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the image to the ImageView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        boolean favorite = CheckPreferences.getDisplayFavoritesFromPreferences(mContext);
        if (favorite) {
            holder.itemView.setTag(mFavoriteMovieEntries.get(position));
            FavoriteMovieEntry favoriteMovieEntry = mFavoriteMovieEntries.get(position);
            Bitmap bitmap = BitmapFactory.decodeByteArray(favoriteMovieEntry.getPoster(), 0, favoriteMovieEntry.getPoster().length);
            holder.myImageView.setImageBitmap(bitmap);
        } else {
            holder.itemView.setTag(mMovies.get(position));
            MoviesModel movie = mMovies.get(position);
            String fullPosterPath = MovieUrlConstants.BASE_POSTER_URL + MovieUrlConstants.DEFAULT_POSTER_SIZE + movie.getPosterPath();
            Picasso.with(mContext).load(fullPosterPath).into(holder.myImageView);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        boolean favorite = CheckPreferences.getDisplayFavoritesFromPreferences(mContext);
        if (favorite) {
                return mFavoriteMovieEntries.size();
        } else {
                return mMovies.size();
        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public MoviesModel getMoviesModelItem(int id) {
        boolean favorite = CheckPreferences.getDisplayFavoritesFromPreferences(mContext);
        if (favorite) {
            return null;
        } else {
            return mMovies.get(id);
        }

    }

    // convenience method for getting data at click position
    public FavoriteMovieEntry getFavoriteMovieEntryItem(int id) {
        boolean favorite = CheckPreferences.getDisplayFavoritesFromPreferences(mContext);
        if (favorite) {
            return mFavoriteMovieEntries.get(id);
        } else {
            return null;
        }

    }

    public void setMovieData(List<MoviesModel> movies, List<FavoriteMovieEntry> favoriteMovieEntries) {
        clear();
        boolean favorite = CheckPreferences.getDisplayFavoritesFromPreferences(mContext);
        if (favorite) {
            mFavoriteMovieEntries = favoriteMovieEntries;
            notifyDataSetChanged();
        } else {
            mMovies = movies;
            notifyDataSetChanged();
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void clear() {
        if (mMovies != null) {
            final int size = mMovies.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    mMovies.remove(0);
                }

                notifyItemRangeRemoved(0, size);
            }
        }
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
