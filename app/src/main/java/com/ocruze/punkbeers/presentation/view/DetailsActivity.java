package com.ocruze.punkbeers.presentation.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ocruze.punkbeers.R;
import com.ocruze.punkbeers.presentation.Constants;
import com.ocruze.punkbeers.presentation.Singletons;
import com.ocruze.punkbeers.presentation.model.beer.Beer;

public class DetailsActivity extends AppCompatActivity {

    private Beer beer;
    private TextView beerName;
    private ImageView beerImage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);

        beerName = findViewById(R.id.beer_name);
        beerImage = findViewById(R.id.beer_image);

        beer = Singletons.getGson().fromJson(getIntent().getStringExtra(Constants.CURRENT_BEER), Beer.class);
        showBeerDetails(beer);
    }

    private void showBeerDetails(Beer beer) {
        beerName.setText(beer.getName());

        if (!beer.getImageUrl().equals(Constants.DEFAULT_BEER_IMG_URL)) {
            Glide
                    .with(getApplicationContext())
                    .load(beer.getImageUrl())
                    .into(beerImage);
        }

    }
}
