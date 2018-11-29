package com.example.android.popmovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popmovies.Data.MovieUrlConstants;
import com.example.android.popmovies.JsonResponseModels.MoviesModel;
import com.example.android.popmovies.R;
import com.example.android.popmovies.Utilities.CheckPreferences;
import com.example.android.popmovies.Utilities.GridUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {


    private List<MoviesModel> mMovies;
    private WebItemClickListener mClickListener;
    private final Context mContext;


    // data is passed into the constructor
    public MovieRecyclerViewAdapter(Context context, List<MoviesModel> movies) {
        this.mMovies = movies;
        this.mContext = context;

    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        params.height = GridUtils.gridItemHeightInPixelsFromWidth(parent.getContext());
        return new ViewHolder(view);
    }

    // binds the image to the ImageView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(mMovies.get(position));
        MoviesModel movie = mMovies.get(position);
        String fullPosterPath = MovieUrlConstants.BASE_POSTER_URL + MovieUrlConstants.DEFAULT_POSTER_SIZE + movie.getPosterPath();
        Picasso.with(mContext).load(fullPosterPath).fit().placeholder(R.drawable.ic_movie_loading).into(holder.myImageView);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mMovies.size();
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
            if (mClickListener != null) mClickListener.onWebItemClick(getAdapterPosition());
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

    public List<MoviesModel> getMovieData() {
        return mMovies;
    }

    public void setMovieData(List<MoviesModel> movies) {
        clear();
        mMovies = movies;
        notifyDataSetChanged();
    }

    public void addMovieData(List<MoviesModel> moreMovies) {
        int curSize = getItemCount();
        mMovies.addAll(moreMovies);
        notifyItemRangeInserted(curSize, moreMovies.size());
    }

    // allows clicks events to be caught
    public void setClickListener(WebItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface WebItemClickListener {
        void onWebItemClick(int position);
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
    }

    public void remove(MoviesModel moviesModel) {
        int position = mMovies.indexOf(moviesModel);
        if (position > -1) {
            mMovies.remove(position);
            notifyItemRemoved(position);
        }
    }


}
