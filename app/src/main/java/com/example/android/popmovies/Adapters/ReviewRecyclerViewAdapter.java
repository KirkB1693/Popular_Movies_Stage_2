package com.example.android.popmovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popmovies.JsonResponseModels.ReviewsModel;
import com.example.android.popmovies.R;

import java.util.List;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {
    private List<ReviewsModel> mReviews;
    private ReviewRecyclerViewAdapter.ReviewItemClickListener mClickListener;
    private final Context mContext;


    // data is passed into the constructor
    public ReviewRecyclerViewAdapter(Context context, List<ReviewsModel> reviews) {
        this.mReviews = reviews;
        this.mContext = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ReviewRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the image to the ImageView in each cell
    @Override
    public void onBindViewHolder(@NonNull ReviewRecyclerViewAdapter.ViewHolder holder, int position) {
        String reviewAuthor = mReviews.get(position).getAuthor();
        String reviewContent = mReviews.get(position).getContent();
        holder.itemView.setTag(reviewAuthor);
        holder.myAuthorTextView.setText(reviewAuthor);
        holder.myContentTextView.setText(reviewContent);

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mReviews.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView myAuthorTextView;
        final TextView myContentTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myAuthorTextView = (TextView) itemView.findViewById(R.id.textViewReviewAuthor);
            myContentTextView = (TextView) itemView.findViewById(R.id.textViewReviewContent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onReviewItemClick(getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mReviews.get(id).getUrl();
    }

    void setReviewData (List<ReviewsModel> reviews){
        mReviews = reviews;
        notifyDataSetChanged();
    }

    // allows clicks events to be caught
    public void setClickListener(ReviewRecyclerViewAdapter.ReviewItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ReviewItemClickListener {
        void onReviewItemClick(int position);
    }


}
