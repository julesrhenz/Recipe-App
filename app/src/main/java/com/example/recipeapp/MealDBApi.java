package com.example.recipeapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealDBApi {

    // Search by dish name
    @GET("search.php")
    Call<MealResponse> searchMealsByName(@Query("s") String query);

    @GET("search.php")
    Call<MealResponse> searchMeals(@Query("s") String query);

    // Filter by ingredient
    @GET("filter.php")
    Call<MealResponse> searchMealsByIngredient(@Query("i") String ingredient);

    // Filter by category
    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("lookup.php")
    Call<MealResponse> getMealById(@Query("i") String mealId);

    @GET("categories.php")
    Call<CategoryResponse> getCategories();
}
