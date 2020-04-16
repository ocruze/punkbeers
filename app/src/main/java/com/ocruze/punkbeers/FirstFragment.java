package com.ocruze.punkbeers;

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
import com.ocruze.punkbeers.beer.Beer;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_first, container, false);
        page = 1;

        makeApiCall(page);

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

        beerListAdapter = new BeerListAdapter(beers, this);
        recyclerView.setAdapter(beerListAdapter);
    }

    private void makeApiCall(int page) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

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


    @Override
    public void onItemClick(int position) {
        Util.showToast(getActivity().getApplicationContext(), position + "");

        /*Bundle bundle = new Bundle();
        bundle.put
         */
        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);// bundle
    }
}
