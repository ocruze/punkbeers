package com.ocruze.punkbeers.presentation.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ocruze.punkbeers.R;
import com.ocruze.punkbeers.presentation.Constants;
import com.ocruze.punkbeers.presentation.Singletons;
import com.ocruze.punkbeers.presentation.controller.MainController;
import com.ocruze.punkbeers.presentation.model.beer.Beer;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BeerListAdapter.OnItemClickListener, BeerListAdapter.OnBottomReachedListener {

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

        ConstraintLayout constraintLayout = findViewById(R.id.main_activity_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(Constants.ANIMATION_DURATION);
        animationDrawable.setExitFadeDuration(Constants.ANIMATION_DURATION);
        animationDrawable.start();

        // non-view related
        controller = new MainController(this, Singletons.getGson(), Singletons.getPunkRepository(this));
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

        beerListAdapter = new BeerListAdapter(beers, this, this, getApplicationContext());
        recyclerView.setAdapter(beerListAdapter);
    }

    public void updateList(List<Beer> beers) {
        if (beers != null && recyclerView != null) {
            int oldSize = beerListAdapter.getBeers().size();
            beerListAdapter.getBeers().addAll(beers);
            recyclerView.setAdapter(beerListAdapter);
            recyclerView.scrollToPosition(oldSize - 5);
        }
    }

    @Override
    public void onItemClick(Beer beer) {
        controller.onItemClick(beer);
    }

    @Override
    public void onBottomReached(int position) {
        controller.onBottomReached();
    }

    public void navigateToDetails(Beer beer) {
        String jsonBeer = Singletons.getGson().toJson(beer, Beer.class);

        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra(Constants.CURRENT_BEER, jsonBeer);
        this.startActivity(detailsIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help) {
            Intent helpIntent = new Intent(this, HelpActivity.class);
            startActivity(helpIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
