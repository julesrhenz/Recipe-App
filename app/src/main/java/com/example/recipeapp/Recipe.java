package com.example.recipeapp;

public class Recipe {
    private int id;
    private String title;
    private String ingredients;
    private String instructions;
    private String category;
    private String imageUrl;

    // Full constructor (with ID)
    public Recipe(int id, String title, String ingredients, String instructions, String category, String imageUrl) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // Constructor without ID (optional, for inserts)
    public Recipe(String title, String ingredients, String instructions, String category, String imageUrl) {
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getIngredients() { return ingredients; }
    public String getInstructions() { return instructions; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public void setCategory(String category) { this.category = category; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
