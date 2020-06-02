package com.ocruze.punkbeers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ocruze.punkbeers.beer.Beer;

import java.lang.reflect.Type;

public class SecondFragment extends Fragment {
    private TextView beerName;
    private ImageView beerImage;

    private Beer beer; // current beer

    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        gson = new GsonBuilder()
                // .registerTypeAdapter(Beer.class, new BeerTypeAdapter())
                .setLenient()
                .create();

        beerName = v.findViewById(R.id.beer_name);
        beerImage = v.findViewById(R.id.beer_image);

        Type beerType = new TypeToken<Beer>() {
        }.getType();

        System.out.println("second : "+ getArguments().getString("beer"));
        beer = gson.fromJson(getArguments().getString("beer"), beerType);
        showBeerDetails(beer);

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
         */
    }

    public void showBeerDetails(Beer beer) {
        //beerName.setText(beer.getName());

        Glide.with(getActivity().getApplicationContext()).load(beer.getImageUrl()).into(beerImage);
    }
}
