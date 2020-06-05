package com.ocruze.punkbeers.presentation.view;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.ocruze.punkbeers.R;
import com.ocruze.punkbeers.presentation.Constants;
import com.ocruze.punkbeers.presentation.Singletons;
import com.ocruze.punkbeers.presentation.model.beer.Beer;
import com.ocruze.punkbeers.presentation.model.beer.Ingredients;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private Beer beer;

    private ImageView image;
    private TextView name, firstBrewed, tagline, description;

    private TextView abv, ibu, ebc, srm;

    private TableLayout tableMalt, tableHops;

    private TextView volume, boilVolume;
    private TextView tempMash, tempFermentation;
    private TextView yeast;
    private TextView foodPairing;
    private TextView brewersTips;
    private TextView contributor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);

        ConstraintLayout constraintLayout = findViewById(R.id.details_activity_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground() ;
        animationDrawable.setEnterFadeDuration(Constants.ANIMATION_DURATION);
        animationDrawable.setExitFadeDuration(Constants.ANIMATION_DURATION);
        animationDrawable.start();

        name = findViewById(R.id.beer_name);
        image = findViewById(R.id.beer_image);
        firstBrewed = findViewById(R.id.detail_beer_first_brewed);
        tagline = findViewById(R.id.detail_beer_tagline);
        description = findViewById(R.id.detail_beer_desc);

        abv = findViewById(R.id.beer_abv);
        ibu = findViewById(R.id.beer_ibu);
        ebc = findViewById(R.id.beer_ebc);
        srm = findViewById((R.id.beer_srm));

        tableMalt = findViewById(R.id.table_malt);
        tableHops = findViewById(R.id.table_hops);

        volume = findViewById(R.id.detail_beer_volume);
        boilVolume = findViewById(R.id.detail_beer_volume_boil);

        tempMash = findViewById(R.id.detail_beer_temp_mash);
        tempFermentation = findViewById(R.id.detail_beer_temp_fermentation);

        yeast = findViewById(R.id.detail_beer_yeast);

        foodPairing = findViewById(R.id.detail_beer_food_pairing);

        brewersTips = findViewById(R.id.detail_beer_brewers_tips);

        contributor = findViewById(R.id.detail_beer_contributor);

        beer = Singletons.getGson().fromJson(getIntent().getStringExtra(Constants.CURRENT_BEER), Beer.class);
        showBeerDetails(beer);
    }

    private void showBeerDetails(Beer beer) {
        if (!beer.getImageUrl().equals(Constants.DEFAULT_BEER_IMG_URL)) {
            Glide
                    .with(getApplicationContext())
                    .load(beer.getImageUrl())
                    .into(image);
        }

        name.setText(beer.getName());
        firstBrewed.setText("First brewed: " + beer.getFirstBrewed());
        tagline.setText(beer.getTagline());
        description.setText(beer.getDescription());

        abv.setText(String.valueOf(beer.getAbv()));
        ibu.setText(String.valueOf(beer.getIbu()));
        ebc.setText(String.valueOf(beer.getEbc()));
        srm.setText(String.valueOf(beer.getSrm()));

        List<Ingredients.Ingredient> malt = beer.getIngredients().getMalt();
        tableMalt.setStretchAllColumns(true);
        for (Ingredients.Ingredient m : malt) {
            TableRow tr = new TableRow(this);
            TextView maltName = new TextView(this);
            maltName.setText(m.getName());
            maltName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            TextView maltAmount = new TextView(this);
            maltAmount.setText(String.valueOf(m.getAmount()));
            maltAmount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            tr.addView(maltName);
            tr.addView(maltAmount);
            tableMalt.addView(tr);
        }

        List<Ingredients.Hop> hops = beer.getIngredients().getHops();
        tableHops.setStretchAllColumns(true);
        for (Ingredients.Hop h : hops) {
            TableRow tr = new TableRow(this);
            TextView hopName = new TextView(this);
            hopName.setText(h.getName());
            hopName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            TextView hopAmount = new TextView(this);
            hopAmount.setText(String.valueOf(h.getAmount()));
            hopAmount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            TextView hopAdd = new TextView(this);
            hopAdd.setText(String.valueOf(h.getAdd()));
            hopAdd.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            TextView hopAttribute = new TextView(this);
            hopAttribute.setText(String.valueOf(h.getAttribute()));
            hopAttribute.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            tr.addView(hopName);
            tr.addView(hopAmount);
            tr.addView(hopAdd);
            tr.addView(hopAttribute);
            tableHops.addView(tr);
        }

        volume.setText("Volume: " + beer.getVolume());
        boilVolume.setText("Boil volume: " + beer.getBoilVolume());

        tempMash.setText("Mash temperature: " + beer.getMethod().getMashTemp().getTemperature());
        tempFermentation.setText("Fermentation temperature: " + beer.getMethod().getFermentation().getTemperature());

        yeast.setText("Yeast: " + beer.getIngredients().getYeast());

        String str = "Best goes with:";
        for (String food : beer.getFoodPairing()) {
            str += "\n\t - " + food;
        }
        foodPairing.setText(str);

        brewersTips.setText("Brewer's tips: " + beer.getBrewersTips());

        contributor.setText("Contributor: " + beer.getContributedBy());
    }
}
