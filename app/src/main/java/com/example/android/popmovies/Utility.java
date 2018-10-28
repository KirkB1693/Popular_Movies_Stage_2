package com.example.android.popmovies;

import android.content.Context;
import android.util.DisplayMetrics;

class Utility {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int posterWidth = (int) (context.getResources().getDimension(R.dimen.movie_poster_width) / displayMetrics.density);
        int noOfColumns = (int) (dpWidth / posterWidth );
        if (noOfColumns == 0){noOfColumns=1;}
        return noOfColumns;
    }



}