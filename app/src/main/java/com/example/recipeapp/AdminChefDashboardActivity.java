package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminChefDashboardActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnAddRecipe, btnViewRecipes, btnSearch, btnManageCategories, btnLogout;
    String role, email;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chef_dashboard);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        btnViewRecipes = findViewById(R.id.btnViewRecipes);
        btnSearch = findViewById(R.id.btnSearch);
        btnManageCategories = findViewById(R.id.btnManageCategories);
        btnLogout = findViewById(R.id.btnLogout);

        role = getIntent().getStringExtra("role");
        email = getIntent().getStringExtra("email");

        tvWelcome.setText("Welcome");

        btnAddRecipe.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditRecipeActivity.class))
        );

        btnViewRecipes.setOnClickListener(v ->
                startActivity(new Intent(this, RecipeListActivity.class))
        );

        btnSearch.setOnClickListener(v ->
                startActivity(new Intent(this, SearchRecipeActivity.class))
        );

        btnManageCategories.setOnClickListener(v ->
                startActivity(new Intent(this, CategoryManagementActivity.class))
        );

        btnLogout.setOnClickListener(v -> {
            // Optionally clear saved session info here if used
            Intent intent = new Intent(AdminChefDashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(intent);
            finish(); // Close current activity
        });
    }
}


