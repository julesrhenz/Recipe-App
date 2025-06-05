package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

public class RecipeListActivity extends AppCompatActivity implements MealAdapter.OnMealClickListener {

    RecyclerView recyclerViewAllRecipes;
    ProgressBar progressBarAllRecipes;
    MealAdapter mealAdapter;
    List<Meal> allMeals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        recyclerViewAllRecipes = findViewById(R.id.recyclerViewAllRecipes);
        recyclerViewAllRecipes.setLayoutManager(new LinearLayoutManager(this));

        progressBarAllRecipes = findViewById(R.id.progressBarAllRecipes);

        loadAllMeals();
    }

    private void loadAllMeals() {
        progressBarAllRecipes.setVisibility(View.VISIBLE);

        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        final int[] responsesReceived = {0};

        for (char c : alphabet) {
            String query = String.valueOf(c);
            ApiClient.getApiService().searchMealsByName(query).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                    responsesReceived[0]++;
                    if (response.isSuccessful() && response.body() != null && response.body().getMeals() != null) {
                        allMeals.addAll(response.body().getMeals());
                    }
                    checkIfFinished(responsesReceived[0]);
                }

                @Override
                public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                    progressBarAllRecipes.setVisibility(View.GONE);

                    String errorMessage = t.getMessage() != null ? t.getMessage() : "Unknown error occurred";
                    Toast.makeText(RecipeListActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", errorMessage, t);
                }
            });
        }
    }

    private void checkIfFinished(int callsCompleted) {
        if (callsCompleted == 26) {
            progressBarAllRecipes.setVisibility(View.GONE);
            if (allMeals.isEmpty()) {
                Toast.makeText(this, "No recipes found.", Toast.LENGTH_SHORT).show();
            } else {
                mealAdapter = new MealAdapter(allMeals, this, this);
                recyclerViewAllRecipes.setAdapter(mealAdapter);
            }
        }
    }

    @Override
    public void onMealClick(Meal meal) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("meal", meal);
        startActivity(intent);
    }

    @Override
    public void onMealLongClick(Meal meal) {
        // Optional: implement long-click behavior
    }
}
