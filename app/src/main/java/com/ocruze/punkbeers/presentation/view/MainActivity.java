package com.ocruze.punkbeers.presentation.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ocruze.punkbeers.R;
import com.ocruze.punkbeers.data.PunkApi;
import com.ocruze.punkbeers.presentation.model.Constants;
import com.ocruze.punkbeers.presentation.model.Util;
import com.ocruze.punkbeers.presentation.model.beer.Beer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BeerListAdapter.OnItemListener {

    private RecyclerView recyclerView;
    private BeerListAdapter beerListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View v;

    private int page;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // view related
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton next = findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Next button action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton previous = findViewById(R.id.btn_previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Previous button action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // non-view related
        page = 1;

        sharedPreferences = getApplicationContext().getSharedPreferences(Constants.APP_PREFS_KEY, Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                //.registerTypeAdapter(Beer.class, new BeerTypeAdapter())
                .setLenient()
                .create();

        if (isConnectedToInternet()) {
            makeApiCall(page);
        } else {
            loadDataFromCache();
        }
    }

    private void showList(List<Beer> beers) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        List<String> input = new ArrayList<>();

        beerListAdapter = new BeerListAdapter(beers, this, getApplicationContext());
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
                    Util.showToast(getApplicationContext(), "API Success : " + response.code());

                    showList(beers);
                    saveList(beers);
                } else {

                    Util.showToast(getApplicationContext(), "API Error : no response : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                Util.showToast(getApplicationContext(), "API Error");
            }
        });
    }

    private void loadDataFromCache() {
        if (!sharedPreferences.contains(Constants.PREFS_KEY_BEERS_LIST)) {
            Util.showToast(getApplicationContext(), "Not connected to internet, no data in cache");
            return;
        }

        String jsonPokemonList = sharedPreferences.getString(Constants.PREFS_KEY_BEERS_LIST, null);
        List<Beer> beers;

        Type beersListType = new TypeToken<List<Beer>>() {
        }.getType();

        beers = gson.fromJson(jsonPokemonList, beersListType);
        showList(beers);
        Util.showToast(getApplicationContext(), "Not connected to internet, loading data from cache");
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
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onItemClick(Beer beer) {
        String jsonBeer = gson.toJson(beer, Beer.class);
        System.out.println("first : " + jsonBeer);
        Bundle bundle = new Bundle();
        bundle.putString("beer", jsonBeer);

        // NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);// bundle
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Util.showToast(getApplicationContext(), "Clicked on settings");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
