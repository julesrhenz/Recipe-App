package com.example.recipeapp;

import java.io.Serializable;

public class Meal implements Serializable {
    // Your fields like idMeal, strMeal, strCategory, strInstructions, etc.
    private String idMeal;
    private String strMeal;
    private String strMealThumb;
    private String strCategory;
    private String strInstructions;
    private String strIngredient1;
    private String strIngredient2;
    private String strIngredient3;
    private String strIngredient4;
    private String strIngredient5;
    // Add all other ingredients, etc.

    // Getter and Setter methods
    public String getIdMeal() { return idMeal; }
    public void setIdMeal(String idMeal) { this.idMeal = idMeal; }
    public String getStrMeal() { return strMeal; }
    public void setStrMeal(String strMeal) { this.strMeal = strMeal; }
    public String getStrCategory() { return strCategory; }
    public void setStrCategory(String strCategory) { this.strCategory = strCategory; }
    public String getStrInstructions() { return strInstructions; }
    public void setStrInstructions(String strInstructions) { this.strInstructions = strInstructions; }
    public String getStrIngredient1() { return strIngredient1; }
    public void setStrIngredient1(String strIngredient1) { this.strIngredient1 = strIngredient1; }
    public String getStrIngredient2() { return strIngredient2; }
    public void setStrIngredient2(String strIngredient2) { this.strIngredient2 = strIngredient2; }
    public String getStrIngredient3() { return strIngredient3; }
    public void setStrIngredient3(String strIngredient3) { this.strIngredient3 = strIngredient3; }
    public String getStrIngredient4() { return strIngredient4; }
    public void setStrIngredient4(String strIngredient4) { this.strIngredient4 = strIngredient4; }
    public String getStrIngredient5() { return strIngredient5; }
    public void setStrIngredient5(String strIngredient5) { this.strIngredient5 = strIngredient5; }
    public String getStrMealThumb() {return strMealThumb;}

    // Add other getters/setters as needed
}
