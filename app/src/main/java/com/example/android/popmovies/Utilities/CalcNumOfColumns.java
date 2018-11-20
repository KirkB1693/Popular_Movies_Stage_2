package com.example.android.popmovies.Utilities;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.android.popmovies.R;

public class CalcNumOfColumns {
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



}