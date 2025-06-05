package com.example.recipeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_INGREDIENTS = "ingredients";
    public static final String COLUMN_INSTRUCTIONS = "instructions";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_IMAGE_URL = "imageUrl";

    public RecipeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT UNIQUE,"
                + COLUMN_INGREDIENTS + " TEXT,"
                + COLUMN_INSTRUCTIONS + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_IMAGE_URL + " TEXT"
                + ")";
        db.execSQL(CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    // Add recipe
    public long addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, recipe.getTitle());
        values.put(COLUMN_INGREDIENTS, recipe.getIngredients());
        values.put(COLUMN_INSTRUCTIONS, recipe.getInstructions());
        values.put(COLUMN_CATEGORY, recipe.getCategory());
        values.put(COLUMN_IMAGE_URL, recipe.getImageUrl());

        long id = db.insert(TABLE_RECIPES, null, values);
        db.close();
        return id;
    }

    // Get recipe by ID
    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null,
                COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

        Recipe recipe = null;
        if (cursor.moveToFirst()) {
            recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTIONS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
            );
        }
        cursor.close();
        db.close();
        return recipe;
    }

    // Get recipe by Title
    public Recipe getRecipeByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null,
                COLUMN_TITLE + "=?", new String[]{title},
                null, null, null);

        Recipe recipe = null;
        if (cursor.moveToFirst()) {
            recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTIONS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
            );
        }
        cursor.close();
        db.close();
        return recipe;
    }

    // Get all recipes
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTIONS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
                );
                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipeList;
    }

    // Update recipe by ID
    public boolean updateRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, recipe.getTitle());
        values.put(COLUMN_INGREDIENTS, recipe.getIngredients());
        values.put(COLUMN_INSTRUCTIONS, recipe.getInstructions());
        values.put(COLUMN_CATEGORY, recipe.getCategory());
        values.put(COLUMN_IMAGE_URL, recipe.getImageUrl());

        int rowsAffected = db.update(TABLE_RECIPES, values,
                COLUMN_ID + " = ?", new String[]{String.valueOf(recipe.getId())});
        db.close();
        return rowsAffected > 0;
    }

    // Update recipe by Title
    public boolean updateRecipeByTitle(String originalTitle, Recipe updatedRecipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, updatedRecipe.getTitle());
        values.put(COLUMN_INGREDIENTS, updatedRecipe.getIngredients());
        values.put(COLUMN_INSTRUCTIONS, updatedRecipe.getInstructions());
        values.put(COLUMN_CATEGORY, updatedRecipe.getCategory());
        values.put(COLUMN_IMAGE_URL, updatedRecipe.getImageUrl());

        int rowsAffected = db.update(TABLE_RECIPES, values,
                COLUMN_TITLE + " = ?", new String[]{originalTitle});
        db.close();
        return rowsAffected > 0;
    }

    // Delete recipe by ID
    public boolean deleteRecipe(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_RECIPES,
                COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }

    // Delete recipe by Title
    public boolean deleteRecipeByTitle(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_RECIPES,
                COLUMN_TITLE + " = ?", new String[]{title});
        db.close();
        return rowsDeleted > 0;
    }

    // ✅ Search recipes by title or ingredients
    public List<Recipe> searchRecipes(String keyword) {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_RECIPES +
                " WHERE " + COLUMN_TITLE + " LIKE ? COLLATE NOCASE OR " +
                COLUMN_INGREDIENTS + " LIKE ? COLLATE NOCASE";

        String keywordPattern = "%" + keyword + "%";

        Cursor cursor = db.rawQuery(query, new String[]{keywordPattern, keywordPattern});
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTRUCTIONS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
                );
                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipeList;
    }

    // Get all distinct categories
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_RECIPES,
                new String[]{COLUMN_CATEGORY},
                null, null, null, null,
                COLUMN_CATEGORY + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                if (category != null && !category.isEmpty() && !categories.contains(category)) {
                    categories.add(category);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categories;
    }
}
