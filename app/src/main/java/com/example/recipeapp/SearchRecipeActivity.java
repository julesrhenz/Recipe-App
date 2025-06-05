package com.example.recipeapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchRecipeActivity extends AppCompatActivity {

    EditText etSearch;
    RecyclerView recyclerView;
    RecipeAdapter adapter;
    RecipeDatabaseHelper dbHelper;
    List<Recipe> recipeList;
    MealDBApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerViewSearchResults);

        dbHelper = new RecipeDatabaseHelper(this);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(MealDBApi.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initial load of all recipes from local database
        recipeList = dbHelper.getAllRecipes();
        adapter = new RecipeAdapter(this, recipeList, dbHelper);
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchRecipes(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchRecipes(String keyword) {
        // Search from local SQLite DB
        List<Recipe> filteredList = dbHelper.searchRecipes(keyword);

        if (!filteredList.isEmpty()) {
            adapter.setRecipes(filteredList);
        } else {
            // If not found locally, search from the Meal API
            apiService.searchMeals(keyword).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getMeals() != null) {
                        List<Meal> meals = response.body().getMeals();
                        List<Recipe> apiRecipes = new ArrayList<>();

                        for (Meal meal : meals) {
                            Recipe recipe = new Recipe(
                                    meal.getStrMeal(),
                                    meal.getStrIngredient1(), // Stub method; can be expanded
                                    meal.getStrInstructions(),
                                    meal.getStrCategory(),
                                    meal.getStrMealThumb()
                            );
                            apiRecipes.add(recipe);
                        }

                        adapter.setRecipes(apiRecipes);
                    } else {
                        adapter.setRecipes(new ArrayList<>());
                        Toast.makeText(SearchRecipeActivity.this, "No recipes found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                    Toast.makeText(SearchRecipeActivity.this, "Failed to fetch from API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
