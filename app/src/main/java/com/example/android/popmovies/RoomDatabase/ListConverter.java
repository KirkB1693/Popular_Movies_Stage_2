package com.example.android.popmovies.RoomDatabase;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListConverter {
    @TypeConverter
    public static String listToString(List<String> list) {
        if (list == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.toJson(list, listType);
    }

    @TypeConverter
    public static List<String> listFromString(String listString) {
        if (listString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(listString, listType);
    }

}
