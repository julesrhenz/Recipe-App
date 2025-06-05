package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesByCategoryActivity extends AppCompatActivity {

    TextView tvCategoryTitle;
    RecyclerView recyclerViewCategoryRecipes;
    MealAdapter mealAdapter;
    String selectedCategory;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_by_category);

        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        recyclerViewCategoryRecipes = findViewById(R.id.recyclerViewCategoryRecipes);
        recyclerViewCategoryRecipes.setLayoutManager(new LinearLayoutManager(this));

        selectedCategory = getIntent().getStringExtra("category");
        tvCategoryTitle.setText("Category: " + selectedCategory);

        fetchMealsByCategory(selectedCategory);
    }

    private void fetchMealsByCategory(String category) {
        MealDBApi api = ApiClient.getApiService();
        Call<MealResponse> call = api.getMealsByCategory(category);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Meal> meals = response.body().getMeals();

                    // Set category manually because filter endpoint doesn't include it
                    for (Meal meal : meals) {
                        meal.setStrCategory(category);
                    }

                    // Pass listener directly to constructor (3-arg constructor)
                    mealAdapter = new MealAdapter(meals, RecipesByCategoryActivity.this, new MealAdapter.OnMealClickListener() {
                        @Override
                        public void onMealClick(Meal meal) {
                            fetchMealDetails(meal.getIdMeal());
                        }

                        @Override
                        public void onMealLongClick(Meal meal) {
                            // Optional: Add long-click behavior if needed
                        }
                    });

                    recyclerViewCategoryRecipes.setAdapter(mealAdapter);
                } else {
                    Toast.makeText(RecipesByCategoryActivity.this, "No meals found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                Toast.makeText(RecipesByCategoryActivity.this, "Failed to fetch meals: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }

    private void fetchMealDetails(String idMeal) {
        MealDBApi api = ApiClient.getApiService();
        Call<MealResponse> call = api.getMealById(idMeal);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getMeals() != null) {
                    Meal mealDetail = response.body().getMeals().get(0);

                    Intent intent = new Intent(RecipesByCategoryActivity.this, RecipeDetailActivity.class);
                    intent.putExtra("meal", mealDetail);
                    startActivity(intent);
                } else {
                    Toast.makeText(RecipesByCategoryActivity.this, "Meal details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                Toast.makeText(RecipesByCategoryActivity.this, "Failed to load details: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
