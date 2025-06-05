package com.example.recipeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddEditRecipeActivity extends AppCompatActivity {

    EditText etTitle, etIngredients, etInstructions, etImageUrl;
    Spinner spinnerCategory;
    Button btnSaveRecipe, btnDeleteRecipe;

    RecipeDatabaseHelper dbHelper;
    boolean isEditMode = false;
    String originalTitle;
    ArrayAdapter<String> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);

        // Initialize UI components
        etTitle = findViewById(R.id.etTitle);
        etIngredients = findViewById(R.id.etIngredients);
        etInstructions = findViewById(R.id.etInstructions);
        etImageUrl = findViewById(R.id.etImageUrl);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe);
        btnDeleteRecipe = findViewById(R.id.btnDeleteRecipe);

        dbHelper = new RecipeDatabaseHelper(this);

        loadCategoriesIntoSpinner();

        // Check if Intent has local DB recipe info first
        if (getIntent().hasExtra("recipe_title")) {
            // Check if this recipe exists in DB (editing mode)
            String titleFromIntent = getIntent().getStringExtra("recipe_title");
            Recipe recipeFromDB = dbHelper.getRecipeByTitle(titleFromIntent);

            if (recipeFromDB != null) {
                // Editing existing recipe from DB
                isEditMode = true;
                originalTitle = recipeFromDB.getTitle();
                loadRecipeData(originalTitle);
            } else {
                // Recipe not found in DB, probably from API, pre-fill form without edit mode
                preFillFromIntent();
                isEditMode = false;
                btnDeleteRecipe.setVisibility(View.GONE); // cannot delete recipe not in DB
            }
        } else {
            // No recipe passed, add mode
            btnDeleteRecipe.setVisibility(View.GONE);
        }

        btnSaveRecipe.setOnClickListener(v -> saveRecipe());

        btnDeleteRecipe.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Delete Recipe")
                .setMessage("Are you sure you want to delete this recipe?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteRecipeByTitle(originalTitle);
                    Toast.makeText(this, "Recipe deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", null)
                .show());
    }

    private void preFillFromIntent() {
        // Get all fields from intent extras and pre-fill form
        String title = getIntent().getStringExtra("recipe_title");
        String ingredients = getIntent().getStringExtra("recipe_ingredients");
        String instructions = getIntent().getStringExtra("recipe_instructions");
        String category = getIntent().getStringExtra("recipe_category");
        String imageUrl = getIntent().getStringExtra("recipe_image_url");

        if (title != null) etTitle.setText(title);
        if (ingredients != null) etIngredients.setText(ingredients);
        if (instructions != null) etInstructions.setText(instructions);
        if (imageUrl != null) etImageUrl.setText(imageUrl);

        if (category != null) {
            int categoryPosition = categoryAdapter.getPosition(category);
            if (categoryPosition >= 0) {
                spinnerCategory.setSelection(categoryPosition);
            }
        }
    }

    private void loadRecipeData(String title) {
        Recipe recipe = dbHelper.getRecipeByTitle(title);
        if (recipe != null) {
            etTitle.setText(recipe.getTitle());
            etIngredients.setText(recipe.getIngredients());
            etInstructions.setText(recipe.getInstructions());
            etImageUrl.setText(recipe.getImageUrl());
            int categoryPosition = categoryAdapter.getPosition(recipe.getCategory());
            if (categoryPosition >= 0) {
                spinnerCategory.setSelection(categoryPosition);
            }
        }
    }

    private void saveRecipe() {
        String title = etTitle.getText().toString().trim();
        String ingredients = etIngredients.getText().toString().trim();
        String instructions = etInstructions.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String imageUrl = etImageUrl.getText().toString().trim();

        if (title.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Recipe recipe = new Recipe(title, ingredients, instructions, category, imageUrl);

        if (isEditMode) {
            boolean updated = dbHelper.updateRecipeByTitle(originalTitle, recipe);
            if (updated) {
                Toast.makeText(this, "Recipe updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            long id = dbHelper.addRecipe(recipe);
            if (id != -1) {
                Toast.makeText(this, "Recipe added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Insert failed", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    private void loadCategoriesIntoSpinner() {
        List<String> categories = dbHelper.getAllCategories();
        if (categories.isEmpty()) {
            categories.add("Uncategorized");
        }
        categories.add("Add new category...");

        categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isFirstLoad = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isFirstLoad && position == parent.getCount() - 1) {
                    showAddCategoryDialog();
                }
                isFirstLoad = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void showAddCategoryDialog() {
        EditText input = new EditText(this);
        input.setHint("Enter new category");

        new AlertDialog.Builder(this)
                .setTitle("Add New Category")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String newCategory = input.getText().toString().trim();
                    if (!newCategory.isEmpty() && categoryAdapter.getPosition(newCategory) == -1) {
                        categoryAdapter.insert(newCategory, categoryAdapter.getCount() - 1);
                        categoryAdapter.notifyDataSetChanged();
                        spinnerCategory.setSelection(categoryAdapter.getPosition(newCategory));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
