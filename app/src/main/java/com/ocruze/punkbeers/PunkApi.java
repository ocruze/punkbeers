package com.ocruze.punkbeers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PunkApi {
    final static String BASE_URI = "https://api.punkapi.com/v2/";

    @GET("beers/")
    Call<List<Beer>> getBeers(@Query("page") int page);

    @GET("beers/{id}")
    Call<List<Beer>> getBeerById(@Path("id") int id);

    @GET("beers/random")
    Call<List<Beer>> getRandomBeer();
}
