package com.ocruze.punkbeers.data;

import com.ocruze.punkbeers.presentation.model.beer.Beer;

import java.util.List;

public interface PunkCallback {
    void onSuccess(List<Beer> beers);

    void onFailure();
}
