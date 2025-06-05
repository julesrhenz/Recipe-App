package com.example.recipeapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserDashboardActivity extends AppCompatActivity {

    TextView tvUserWelcome;
    Button btnBrowseByCategory, btnSearchRecipeUser, btnFavorites, btnLogout;
    String email;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        tvUserWelcome = findViewById(R.id.tvUserWelcome);
        btnBrowseByCategory = findViewById(R.id.btnBrowseByCategory);
        btnSearchRecipeUser = findViewById(R.id.btnSearchRecipeUser);
        btnFavorites = findViewById(R.id.btnFavorites);
        btnLogout = findViewById(R.id.btnLogout); // 🔹 Link the Logout button

        email = getIntent().getStringExtra("email");
        tvUserWelcome.setText("Welcome User");

        btnBrowseByCategory.setOnClickListener(v ->
                startActivity(new Intent(this, BrowseCategoryActivity.class))
        );

        btnSearchRecipeUser.setOnClickListener(v ->
                startActivity(new Intent(this, SearchRecipeActivity.class))
        );

        btnFavorites.setOnClickListener(v ->
                startActivity(new Intent(this, FavoriteRecipesActivity.class))
        );

        btnLogout.setOnClickListener(v -> {
            // Optionally clear saved session info here if used
            Intent intent = new Intent(UserDashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(intent);
            finish(); // Close current activity
        });
    }
}
