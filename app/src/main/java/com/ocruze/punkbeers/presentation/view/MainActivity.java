package com.ocruze.punkbeers.presentation.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ocruze.punkbeers.R;
import com.ocruze.punkbeers.presentation.Constants;
import com.ocruze.punkbeers.presentation.Singletons;
import com.ocruze.punkbeers.presentation.controller.MainController;
import com.ocruze.punkbeers.presentation.model.Util;
import com.ocruze.punkbeers.presentation.model.beer.Beer;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BeerListAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private BeerListAdapter beerListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private MainController controller;

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
        controller = new MainController(this, Singletons.getSharedPreferences(this), Singletons.getGson());
        controller.onStart();
    }

    public void gradientAnimation(ConstraintLayout layout) {
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();
    }

    public void showList(List<Beer> beers) {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        beerListAdapter = new BeerListAdapter(beers, this, getApplicationContext());
        recyclerView.setAdapter(beerListAdapter);
    }

    @Override
    public void onItemClick(Beer beer) {
        controller.onItemClick(beer);
    }

    public void navigateToDetails(Beer beer) {
        String jsonBeer = Singletons.getGson().toJson(beer, Beer.class);

        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra(Constants.CURRENT_BEER, jsonBeer);
        this.startActivity(detailsIntent);
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
