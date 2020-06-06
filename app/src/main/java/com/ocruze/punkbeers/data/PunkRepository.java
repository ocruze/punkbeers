package com.ocruze.punkbeers.data;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ocruze.punkbeers.presentation.Constants;
import com.ocruze.punkbeers.presentation.model.beer.Beer;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PunkRepository {
    private Gson gson;
    private PunkApi punkApi;
    private SharedPreferences sharedPreferences;

    public PunkRepository(PunkApi punkApi, SharedPreferences sharedPreferences, Gson gson) {
        this.punkApi = punkApi;
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    public void getBeers(final PunkCallback callback) {
        getBeers(callback);
    }

    public void getBeers(final PunkCallback callback, int page) {
        punkApi.getBeers(page).enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    public void getBeerByName(final PunkCallback callback, String beerName) {
        punkApi.getBeerByName(beerName).enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    public List<Beer> loadDataFromCache() {
        if (!sharedPreferences.contains(Constants.PREFS_KEY_BEERS_LIST)) {
            System.out.println("Not connected to internet, no data in cache");
            return null;
        }

        String jsonPokemonList = sharedPreferences.getString(Constants.PREFS_KEY_BEERS_LIST, null);

        Type beersListType = new TypeToken<List<Beer>>() {
        }.getType();

        System.out.println("Not connected to internet, loading data from cache");
        return gson.fromJson(jsonPokemonList, beersListType);
    }

    public void saveList(List<Beer> beers) {
        String jsonBeersList = gson.toJson(beers);

        sharedPreferences.edit()
                .putString(Constants.PREFS_KEY_BEERS_LIST, jsonBeersList)
                .apply();
    }
}
