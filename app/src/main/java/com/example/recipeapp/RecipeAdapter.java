package com.example.recipeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;    // <-- Added
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;   // <-- Added Glide import

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final Context context;
    private List<Recipe> recipeList;
    private final RecipeDatabaseHelper dbHelper;

    public RecipeAdapter(Context context, List<Recipe> recipeList, RecipeDatabaseHelper dbHelper) {
        this.context = context;
        this.recipeList = recipeList;
        this.dbHelper = dbHelper;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory;
        ImageButton btnDelete;
        ImageView imgThumbnail;   // <-- Added

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);  // <-- Added
        }
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.tvTitle.setText(recipe.getTitle());
        holder.tvCategory.setText(recipe.getCategory());

        // Load thumbnail with Glide
        Glide.with(context)
                .load(recipe.getImageUrl())           // load image URL from Recipe model
                .placeholder(R.drawable.placeholder)  // optional placeholder image in drawable folder
                .error(R.drawable.error_image)        // optional error image in drawable folder
                .into(holder.imgThumbnail);

        // Set click listener for editing (on the entire item view except the delete button)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditRecipeActivity.class);
            intent.putExtra("recipe_title", recipe.getTitle());
            intent.putExtra("recipe_ingredients", recipe.getIngredients());
            intent.putExtra("recipe_instructions", recipe.getInstructions());
            intent.putExtra("recipe_category", recipe.getCategory());
            intent.putExtra("recipe_image_url", recipe.getImageUrl());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Delete Recipe")
                .setMessage("Are you sure you want to delete \"" + recipe.getTitle() + "\"?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteRecipeByTitle(recipe.getTitle());
                    recipeList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, recipeList.size());
                    Toast.makeText(context, "Recipe deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void setRecipes(List<Recipe> newRecipes) {
        this.recipeList = newRecipes;
        notifyDataSetChanged();
    }
}
