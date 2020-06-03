package com.ocruze.punkbeers.presentation.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ocruze.punkbeers.presentation.Singletons;
import com.ocruze.punkbeers.presentation.Constants;
import com.ocruze.punkbeers.presentation.model.Util;
import com.ocruze.punkbeers.presentation.model.beer.Beer;
import com.ocruze.punkbeers.presentation.view.MainActivity;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainController {

    private SharedPreferences sharedPreferences;
    private Gson gson;
    private int page;

    private MainActivity view;

    public MainController(MainActivity mainActivity, SharedPreferences sharedPreferences, Gson gson) {
        view = mainActivity;
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    public void onStart() {
        page = 1;

        if (isConnectedToInternet()) {
            makeApiCall(page);
        } else {
            loadDataFromCache();
        }
    }

    private void makeApiCall(int page) {


        Call<List<Beer>> call = Singletons.getPunkApi().getBeers(page);
        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Beer> beers = response.body();
                    Util.showToast(view.getApplicationContext(), "API Success : " + response.code());

                    view.showList(beers);
                    saveList(beers);
                } else {

                    Util.showToast(view.getApplicationContext(), "API Error : no response : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                Util.showToast(view.getApplicationContext(), "API Error");
            }
        });
    }

    private void loadDataFromCache() {
        if (!sharedPreferences.contains(Constants.PREFS_KEY_BEERS_LIST)) {
            Util.showToast(view.getApplicationContext(), "Not connected to internet, no data in cache");
            return;
        }

        String jsonPokemonList = sharedPreferences.getString(Constants.PREFS_KEY_BEERS_LIST, null);
        List<Beer> beers;

        Type beersListType = new TypeToken<List<Beer>>() {
        }.getType();

        beers = gson.fromJson(jsonPokemonList, beersListType);
        view.showList(beers);
        Util.showToast(view.getApplicationContext(), "Not connected to internet, loading data from cache");
    }

    private void saveList(List<Beer> beers) {
        String jsonBeersList = gson.toJson(beers);

        sharedPreferences.edit()
                .putString(Constants.PREFS_KEY_BEERS_LIST, jsonBeersList)
                .apply();

        // Toast.makeText(getApplicationContext(), "Data cached", Toast.LENGTH_SHORT).show();

    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) view.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void onItemClick(Beer beer) {
        String jsonBeer = gson.toJson(beer, Beer.class);
        System.out.println("first : " + jsonBeer);
        Bundle bundle = new Bundle();
        bundle.putString("beer", jsonBeer);

        // NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);// bundle
    }

    public void onButtonClick() {

    }
}
