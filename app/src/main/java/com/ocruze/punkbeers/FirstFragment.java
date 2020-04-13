package com.ocruze.punkbeers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstFragment extends Fragment {

    private RecyclerView recyclerView;
    private BeerListAdapter beerListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_first, container, false);


        makeApiCall();


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

        beerListAdapter = new BeerListAdapter(beers);
        recyclerView.setAdapter(beerListAdapter);
    }

    private void makeApiCall() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PunkApi.BASE_URI)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PunkApi punkApi = retrofit.create(PunkApi.class);

        Call<List<Beer>> call = punkApi.getBeers();
        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Beer> beers = response.body();
                    Util.showToast(getActivity().getApplicationContext(), "API Success : " + response.code());
                    System.out.println(beers);
                    showList(beers);
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


}
