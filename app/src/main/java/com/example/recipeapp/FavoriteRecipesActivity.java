package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteRecipesActivity extends AppCompatActivity implements MealAdapter.OnMealClickListener {

    RecyclerView recyclerViewFavorites;
    TextView tvNoFavorites;
    MealAdapter mealAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_recipes);

        FavoriteManager.init(this);

        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites);
        tvNoFavorites = findViewById(R.id.tvNoFavorites); // <-- bind the "No favorites" text

        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        List<Meal> favorites = FavoriteManager.getFavorites();

        if (favorites.isEmpty()) {
            tvNoFavorites.setVisibility(View.VISIBLE);
            recyclerViewFavorites.setVisibility(View.GONE);
        } else {
            tvNoFavorites.setVisibility(View.GONE);
            recyclerViewFavorites.setVisibility(View.VISIBLE);

            mealAdapter = new MealAdapter(favorites, this, this);
            recyclerViewFavorites.setAdapter(mealAdapter);
        }
    }

    @Override
    public void onMealClick(Meal meal) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("meal", meal);
        intent.putExtra("fromFavorites", true);
        startActivity(intent);
    }

    @Override
    public void onMealLongClick(Meal meal) {
        FavoriteManager.removeFromFavorites(meal);
        Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        loadFavorites();
    }
}
