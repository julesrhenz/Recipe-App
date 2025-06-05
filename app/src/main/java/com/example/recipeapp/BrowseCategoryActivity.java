package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrowseCategoryActivity extends AppCompatActivity {

    ListView listViewCategories;
    ArrayList<String> categoryNames = new ArrayList<>();
    ArrayAdapter<String> adapter;
    List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_category);

        listViewCategories = findViewById(R.id.listViewCategories);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryNames);
        listViewCategories.setAdapter(adapter);

        fetchCategoriesFromApi();

        listViewCategories.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCategory = categoryNames.get(position);

            Intent intent = new Intent(BrowseCategoryActivity.this, RecipesByCategoryActivity.class);
            intent.putExtra("category", selectedCategory);
            startActivity(intent);
        });
    }

    private void fetchCategoriesFromApi() {
        MealDBApi api = ApiClient.getApiService();
        Call<CategoryResponse> call = api.getCategories();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body().getCategories();
                    for (Category category : categoryList) {
                        categoryNames.add(category.getStrCategory());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(BrowseCategoryActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                Toast.makeText(BrowseCategoryActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }
}
