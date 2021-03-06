package com.ocruze.punkbeers.presentation.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.ocruze.punkbeers.data.PunkApi;
import com.ocruze.punkbeers.data.PunkCallback;
import com.ocruze.punkbeers.data.PunkRepository;
import com.ocruze.punkbeers.presentation.model.Util;
import com.ocruze.punkbeers.presentation.model.beer.Beer;
import com.ocruze.punkbeers.presentation.view.MainActivity;

import java.util.List;

public class MainController {

    private PunkRepository repository;
    private Gson gson;
    private int page = 1;

    private MainActivity view;

    public MainController(MainActivity mainActivity, Gson gson, PunkRepository repository) {
        view = mainActivity;
        this.repository = repository;
        this.gson = gson;
    }

    public void onStart() {
        page = 1;

        if (isConnectedToInternet()) {
            repository.getBeers(new PunkCallback() {
                @Override
                public void onSuccess(List<Beer> beers) {
                    view.showList(beers);
                    repository.saveList(beers);
                }

                @Override
                public void onFailure() {
                    Util.showToast(view.getApplicationContext(), "API Error");
                }
            }, page);
        } else {
            view.showList(repository.loadDataFromCache());
        }
    }


    private boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) view.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void onItemClick(Beer beer) {
        view.navigateToDetails(beer);
    }

    public void onBottomReached() {
        if (page > PunkApi.MAXIMUM_PAGE) {
            return;
        }

        page++;
        if (isConnectedToInternet()) {
            repository.getBeers(new PunkCallback() {
                @Override
                public void onSuccess(List<Beer> beers) {
                    view.updateList(beers);
                    repository.saveList(beers);
                }

                @Override
                public void onFailure() {
                    Util.showToast(view.getApplicationContext(), "API Error");
                }
            }, page);
        }
    }


    public void searchByName(String beerName) {
        if (!isConnectedToInternet()) {
            Util.showToast(view, "Not connected to the internet");
        } else {
            repository.getBeerByName(new PunkCallback() {
                @Override
                public void onSuccess(List<Beer> beers) {
                    view.showSearchResults(beers);
                }

                @Override
                public void onFailure() {
                    Util.showToast(view.getApplicationContext(), "API Error");
                }
            }, beerName);
        }
    }
}
