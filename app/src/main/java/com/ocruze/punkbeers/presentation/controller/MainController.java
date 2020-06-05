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
        if (isConnectedToInternet()) {
            repository.getPunkResponse(new PunkCallback() {
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
            repository.loadDataFromCache();
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

    public void onBottomReached(int position) {
        /*int currentPage = ((position + 1) % PunkApi.RESULTS_PER_PAGE) + 1;
        System.out.println("bottom reached " + position);
        System.out.println("current page " + currentPage);*/
        if (page > PunkApi.MAXIMUM_PAGE) {
            return;
        }

        page++;
        if (isConnectedToInternet()) {
            repository.getPunkResponse(new PunkCallback() {
                @Override
                public void onSuccess(List<Beer> beers) {
                    // view.showList(beers);
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

    public void onButtonClick() {

    }

}
