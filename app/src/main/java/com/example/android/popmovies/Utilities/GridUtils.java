package com.example.android.popmovies.Utilities;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.android.popmovies.R;

public class GridUtils {
    private static int remaining;
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int posterWidth = (int) (context.getResources().getDimension(R.dimen.movie_poster_width) / displayMetrics.density);
        int noOfColumns = (int) (dpWidth / posterWidth );
        if (noOfColumns == 0){noOfColumns=1;}
        remaining = (int) (displayMetrics.widthPixels - (noOfColumns * context.getResources().getDimension(R.dimen.movie_poster_width)));
        if (remaining / (noOfColumns + 1) < ((noOfColumns+1))) {
            noOfColumns--;
            remaining = (int) (displayMetrics.widthPixels - (noOfColumns * context.getResources().getDimension(R.dimen.movie_poster_width)));
        }
        return noOfColumns;
    }
    public static int calculateSpacing(Context context) {

        int numberOfColumns = calculateNoOfColumns(context);

        return remaining / (numberOfColumns + 1);
    }

    public static int gridItemWidthInPixels(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float pixelWidth = displayMetrics.widthPixels;
        int noOfColumns = calculateNoOfColumns(context);
        return (int) (pixelWidth/noOfColumns);
    }

    public static int gridItemHeightInPixelsFromWidth(Context context) {
        // based on a 2x3 ratio have height equal to 1.5 times width
        return (int) (gridItemWidthInPixels(context) * 1.5);
    }

}