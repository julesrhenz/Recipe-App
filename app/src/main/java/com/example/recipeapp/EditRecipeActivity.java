package com.example.recipeapp;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class EditRecipeActivity extends AppCompatActivity {

    EditText etTitle, etCategory, etIngredients, etInstructions, etImageUrl;
    Button btnUpdate, btnDelete;

    RecipeDatabaseHelper dbHelper;
    int recipeId;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        etTitle = findViewById(R.id.etTitle);
        etCategory = findViewById(R.id.etCategory);
        etIngredients = findViewById(R.id.etIngredients);
        etInstructions = findViewById(R.id.etInstructions);
        etImageUrl = findViewById(R.id.etImageUrl);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        dbHelper = new RecipeDatabaseHelper(this);
        recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "Invalid recipe ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recipe = dbHelper.getRecipeById(recipeId);
        if (recipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etTitle.setText(recipe.getTitle());
        etCategory.setText(recipe.getCategory());
        etIngredients.setText(recipe.getIngredients());
        etInstructions.setText(recipe.getInstructions());
        etImageUrl.setText(recipe.getImageUrl());

        btnUpdate.setOnClickListener(v -> {
            recipe.setTitle(etTitle.getText().toString().trim());
            recipe.setCategory(etCategory.getText().toString().trim());
            recipe.setIngredients(etIngredients.getText().toString().trim());
            recipe.setInstructions(etInstructions.getText().toString().trim());
            recipe.setImageUrl(etImageUrl.getText().toString().trim());

            boolean success = dbHelper.updateRecipe(recipe);
            Toast.makeText(this, success ? "Recipe updated" : "Update failed", Toast.LENGTH_SHORT).show();
            if (success) finish();
        });

        btnDelete.setOnClickListener(v -> new android.app.AlertDialog.Builder(this)
                .setTitle("Delete Recipe")
                .setMessage("Are you sure you want to delete this recipe?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean success = dbHelper.deleteRecipe(recipeId);
                    Toast.makeText(this, success ? "Recipe deleted" : "Delete failed", Toast.LENGTH_SHORT).show();
                    if (success) finish();
                })
                .setNegativeButton("Cancel", null)
                .show());
    }
}
