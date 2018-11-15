package com.example.android.popmovies.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.popmovies.Data.MovieUrlConstants;
import com.example.android.popmovies.R;

public class CheckPreferences {
    public static String getSortOrderFromPreferences(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForSortOrder = context.getString(R.string.sp_key_sort_order);
        String defaultSortOrder = context.getString(R.string.array_value_most_popular);
        String preferredSortOrder = sp.getString(keyForSortOrder, defaultSortOrder);

        if (preferredSortOrder.equals(defaultSortOrder)) {
            return MovieUrlConstants.SORT_BY_DEFAULT;
        } else {
            return MovieUrlConstants.SORT_BY_HIGHEST_RATED;
        }
    }

    public static boolean getDisplayFavoritesFromPreferences(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForDisplayFavorites = context.getString(R.string.sp_key_show_favorites);
        boolean defaultDisplayFavorites = context.getResources().getBoolean(R.bool.pref_show_favorite_default);

        return sp.getBoolean(keyForDisplayFavorites, defaultDisplayFavorites);
    }
}
