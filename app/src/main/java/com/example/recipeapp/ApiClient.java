package com.example.recipeapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static MealDBApi apiService;

    public static MealDBApi getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(MealDBApi.class);
        }
        return apiService;
    }
}
