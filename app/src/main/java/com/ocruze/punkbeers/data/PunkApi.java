package com.ocruze.punkbeers.data;

import com.ocruze.punkbeers.presentation.model.beer.Beer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PunkApi {
    String BASE_URI = "https://api.punkapi.com/v2/";
    int RESULTS_PER_PAGE = 25;
    int MAXIMUM_PAGE = 5;

    @GET("beers/")
    Call<List<Beer>> getBeers();

    @GET("beers/")
    Call<List<Beer>> getBeers(@Query("page") int page);

    @GET("beers/")
    Call<List<Beer>> getBeerById(@Query("id") int id);

    @GET("beers/")
    Call<List<Beer>> getBeerByName(@Query("beer_name") String beerName);

    @GET("beers/random")
    Call<List<Beer>> getRandomBeer();
}
