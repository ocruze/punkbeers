package com.ocruze.punkbeers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ocruze.punkbeers.beer.Beer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FirstFragment extends Fragment implements BeerListAdapter.OnItemListener {
    private RecyclerView recyclerView;
    private BeerListAdapter beerListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View v;

    private int page;

    private SharedPreferences sharedPreferences;

    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_first, container, false);
        page = 1;

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Constants.APP_PREFS_KEY, Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                //.registerTypeAdapter(Beer.class, new BeerTypeAdapter())
                .setLenient()
                .create();

        if (isConnectedToInternet()) {
            makeApiCall(page);
        } else {
            loadDataFromCache();
        }


        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
         */
    }

    private void showList(List<Beer> beers) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        List<String> input = new ArrayList<>();

        beerListAdapter = new BeerListAdapter(beers, this, getActivity().getApplicationContext());
        recyclerView.setAdapter(beerListAdapter);

    }

    private void makeApiCall(int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PunkApi.BASE_URI)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PunkApi punkApi = retrofit.create(PunkApi.class);

        Call<List<Beer>> call = punkApi.getBeers(page);
        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Beer> beers = response.body();
                    Util.showToast(getActivity().getApplicationContext(), "API Success : " + response.code());

                    showList(beers);
                    saveList(beers);
                } else {

                    Util.showToast(getActivity().getApplicationContext(), "API Error : no response : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                Util.showToast(getActivity().getApplicationContext(), "API Error");
            }
        });
    }

    private void loadDataFromCache() {
        if (!sharedPreferences.contains(Constants.PREFS_KEY_BEERS_LIST)) {
            Util.showToast(getActivity().getApplicationContext(), "Not connected to internet, no data in cache");
            return;
        }

        String jsonPokemonList = sharedPreferences.getString(Constants.PREFS_KEY_BEERS_LIST, null);
        List<Beer> beers;

        Type beersListType = new TypeToken<List<Beer>>() {
        }.getType();

        beers = gson.fromJson(jsonPokemonList, beersListType);
        showList(beers);
        Util.showToast(getActivity().getApplicationContext(), "Not connected to internet, loading data from cache");
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
                (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onItemClick(Beer beer) {
        String jsonBeer = gson.toJson(beer, Beer.class);
        System.out.println("first : " + jsonBeer);
        Bundle bundle = new Bundle();
        bundle.putString("beer", jsonBeer);

        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);// bundle
    }
}
