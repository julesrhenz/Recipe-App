package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    TextView tvDetailTitle, tvDetailCategory, tvDetailIngredients, tvDetailInstructions;
    Button btnToggleFavorite;
    Meal selectedMeal;
    boolean isFavorite = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        FavoriteManager.init(this);

        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailIngredients = findViewById(R.id.tvDetailIngredients);
        tvDetailInstructions = findViewById(R.id.tvDetailInstructions);
        btnToggleFavorite = findViewById(R.id.btnToggleFavorite);

        selectedMeal = (Meal) getIntent().getSerializableExtra("meal");

        if (selectedMeal != null) {
            tvDetailTitle.setText(selectedMeal.getStrMeal());
            tvDetailCategory.setText("Category: " + selectedMeal.getStrCategory());

            StringBuilder ingredientsBuilder = new StringBuilder();
            addIngredient(ingredientsBuilder, selectedMeal.getStrIngredient1());
            addIngredient(ingredientsBuilder, selectedMeal.getStrIngredient2());
            addIngredient(ingredientsBuilder, selectedMeal.getStrIngredient3());
            // Add more ingredients if needed

            tvDetailIngredients.setText(ingredientsBuilder.toString().trim());
            tvDetailInstructions.setText(selectedMeal.getStrInstructions());

            isFavorite = FavoriteManager.isFavorite(selectedMeal);
            updateButtonText();

            btnToggleFavorite.setOnClickListener(v -> {
                if (isFavorite) {
                    FavoriteManager.removeFromFavorites(selectedMeal);
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    isFavorite = false;
                } else {
                    FavoriteManager.addToFavorites(selectedMeal);
                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
                    isFavorite = true;
                }
                updateButtonText();
            });
        }
    }

    private void addIngredient(StringBuilder builder, String ingredient) {
        if (ingredient != null && !ingredient.trim().isEmpty()) {
            builder.append("- ").append(ingredient.trim()).append("\n");
        }
    }

    private void updateButtonText() {
        btnToggleFavorite.setText(isFavorite ? "Remove from Favorites" : "Add to Favorites");
    }
}
