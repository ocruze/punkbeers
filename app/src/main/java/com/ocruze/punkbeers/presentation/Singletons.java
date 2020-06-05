package com.ocruze.punkbeers.presentation;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ocruze.punkbeers.data.PunkApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Singletons {

    private static Gson gsonInstance;
    private static PunkApi punkApiInstance;
    private static SharedPreferences sharedPreferencesInstance;

    public static Gson getGson() {
        if (gsonInstance == null)
            gsonInstance = new GsonBuilder()
                    .setLenient()
                    .create();
        return gsonInstance;
    }

    public static PunkApi getPunkApi() {
        if (punkApiInstance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(PunkApi.BASE_URI)
                    .addConverterFactory(GsonConverterFactory.create(Singletons.gsonInstance))
                    .build();
            punkApiInstance = retrofit.create(PunkApi.class);
        }
        return punkApiInstance;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferencesInstance == null)
            sharedPreferencesInstance = context.getApplicationContext().getSharedPreferences(Constants.APP_PREFS_KEY, Context.MODE_PRIVATE);
        return sharedPreferencesInstance;
    }
}
