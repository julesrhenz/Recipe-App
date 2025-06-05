package com.example.recipeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private final List<Meal> mealList;
    private final Context context;
    private final OnMealClickListener listener;

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
        void onMealLongClick(Meal meal);
    }

    public MealAdapter(List<Meal> mealList, Context context, OnMealClickListener listener) {
        this.mealList = mealList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.tvMealName.setText(meal.getStrMeal());

        // Load thumbnail image using Glide
        Glide.with(context)
                .load(meal.getStrMealThumb())  // thumbnail URL from Meal API
                .placeholder(R.drawable.placeholder) // optional placeholder
                .error(R.drawable.error_image)       // optional error drawable
                .into(holder.imgMealThumb);

        holder.itemView.setOnClickListener(v -> listener.onMealClick(meal));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onMealLongClick(meal);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMealThumb;
        TextView tvMealName;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMealThumb = itemView.findViewById(R.id.imgMealThumb);
            tvMealName = itemView.findViewById(R.id.tvMealName);
        }
    }
}
