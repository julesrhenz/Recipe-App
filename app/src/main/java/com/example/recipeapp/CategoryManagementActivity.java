package com.example.recipeapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryManagementActivity extends AppCompatActivity {

    EditText etNewCategory;
    Button btnAddCategory;
    ListView listViewCategories;
    ProgressBar progressBarCategories;
    TextView tvCategoryHeader;

    ArrayAdapter<String> categoryAdapter;
    List<String> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management); // Match your layout name

        etNewCategory = findViewById(R.id.etNewCategory);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        listViewCategories = findViewById(R.id.listViewCategories);
        progressBarCategories = findViewById(R.id.progressBarCategories);
        tvCategoryHeader = findViewById(R.id.tvCategoryHeader);

        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryList);
        listViewCategories.setAdapter(categoryAdapter);

        // Add category
        btnAddCategory.setOnClickListener(v -> {
            String newCategory = etNewCategory.getText().toString().trim();
            if (!newCategory.isEmpty()) {
                categoryList.add(newCategory);
                categoryAdapter.notifyDataSetChanged();
                etNewCategory.setText("");
                Toast.makeText(this, "Category added manually", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enter category name", Toast.LENGTH_SHORT).show();
            }
        });

        // Remove category on long click
        listViewCategories.setOnItemLongClickListener((parent, view, position, id) -> {
            String categoryToRemove = categoryList.get(position);
            new AlertDialog.Builder(this)
                    .setTitle("Remove Category")
                    .setMessage("Are you sure you want to remove \"" + categoryToRemove + "\"?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        categoryList.remove(position);
                        categoryAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Category removed", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });

        // Load initial categories from API
        loadCategoriesFromAPI();
    }

    private void loadCategoriesFromAPI() {
        progressBarCategories.setVisibility(View.VISIBLE);

        MealDBApi api = ApiClient.getApiService();
        api.getCategories().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                progressBarCategories.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getCategories() != null) {
                    for (Category category : response.body().getCategories()) {
                        if (!categoryList.contains(category.getStrCategory())) {
                            categoryList.add(category.getStrCategory());
                        }
                    }
                    categoryAdapter.notifyDataSetChanged();
                    tvCategoryHeader.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(CategoryManagementActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                progressBarCategories.setVisibility(View.GONE);
                Toast.makeText(CategoryManagementActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
