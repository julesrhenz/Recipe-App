package com.example.recipeapp;

public class User {
    private final String email;
    private final String hashedPassword;
    private final String role;

    public User(String email, String hashedPassword, String role) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getRole() {
        return role;
    }
}
