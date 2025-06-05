package com.example.recipeapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteManager {

    private static final String PREFS_NAME = "favorite_prefs";
    private static final String KEY_FAVORITES = "favorites";

    private static SharedPreferences sharedPreferences;
    private static final Gson gson = new Gson();

    public static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
    }

    public static List<Meal> getFavorites() {
        String json = sharedPreferences.getString(KEY_FAVORITES, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<Meal>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static boolean isFavorite(Meal meal) {
        List<Meal> favorites = getFavorites();
        for (Meal m : favorites) {
            if (m.getIdMeal().equals(meal.getIdMeal())) {
                return true;
            }
        }
        return false;
    }

    public static void addToFavorites(Meal meal) {
        List<Meal> favorites = getFavorites();
        if (!isFavorite(meal)) {
            favorites.add(meal);
            saveFavorites(favorites);
        }
    }

    public static void removeFromFavorites(Meal meal) {
        List<Meal> favorites = getFavorites();
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).getIdMeal().equals(meal.getIdMeal())) {
                favorites.remove(i);
                saveFavorites(favorites);
                break;
            }
        }
    }

    private static void saveFavorites(List<Meal> favorites) {
        String json = gson.toJson(favorites);
        sharedPreferences.edit().putString(KEY_FAVORITES, json).apply();
    }
}
