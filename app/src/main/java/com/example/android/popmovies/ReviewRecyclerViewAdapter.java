package com.example.android.popmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {
    private List<String> mReviewAuthors;
    private List<String> mReviewContent;
    private List<String> mReviewUrl;
    private ReviewRecyclerViewAdapter.ReviewItemClickListener mClickListener;
    private final Context mContext;


    // data is passed into the constructor
    ReviewRecyclerViewAdapter(Context context, List<String> reviewAuthors, List<String> reviewContent, List<String> reviewUrl) {
        this.mReviewAuthors = reviewAuthors;
        this.mReviewContent = reviewContent;
        this.mReviewUrl = reviewUrl;
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
        String reviewAuthor = mReviewAuthors.get(position);
        String reviewContent = mReviewContent.get(position);
        holder.itemView.setTag(reviewAuthor);
        holder.myAuthorTextView.setText(reviewAuthor);
        holder.myContentTextView.setText(reviewContent);

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mReviewAuthors.size();
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
    String getItem(int id) {
        return mReviewUrl.get(id);
    }

    void setReviewData (List<String> reviewAuthors, List<String> reviewContent, List<String> reviewUrl){
        mReviewAuthors = reviewAuthors;
        mReviewContent = reviewContent;
        mReviewUrl = reviewUrl;
        notifyDataSetChanged();
    }

    // allows clicks events to be caught
    void setClickListener(ReviewRecyclerViewAdapter.ReviewItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ReviewItemClickListener {
        void onReviewItemClick(int position);
    }


}
