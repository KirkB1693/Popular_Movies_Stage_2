package com.example.android.popmovies.Utilities;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String LOG_TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    /**
     * The total number of items in the dataset after the last load
     */
    private int mPreviousTotal = 0;
    /**
     * True if we are still waiting for the last set of data to load.
     */
    private boolean mLoading = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
        int firstVisibleItem = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        if (mLoading) {
            if (totalItemCount > mPreviousTotal) {
                mLoading = false;
                mPreviousTotal = totalItemCount;
            }
        }
        int visibleThreshold = 5;
        if (!mLoading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold) && !isLastPage()) {
            // End has been reached

            onLoadMore();

            mLoading = true;
        }
    }

    protected abstract void onLoadMore();

    protected abstract boolean isLastPage();


}