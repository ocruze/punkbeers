package com.ocruze.punkbeers.presentation.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ocruze.punkbeers.R;
import com.ocruze.punkbeers.presentation.model.beer.Beer;

import java.util.List;

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.ViewHolder> {
    private List<Beer> beers;

    private OnItemClickListener onItemClickListener;

    private Context context;

    public BeerListAdapter(List<Beer> beers, OnItemClickListener onItemClickListener, Context context) {
        this.beers = beers;
        this.onItemClickListener = onItemClickListener;
        this.context = context;

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View layout;

        TextView txtBeerName;
        TextView txtBeerTagline;
        ImageView icon;

        TextView txtBeerAbv;
        TextView txtBeerIbu;
        TextView txtBeerEbc;
        TextView txtBeerSrm;


        OnItemClickListener onItemClickListener;

        ViewHolder(View v, OnItemClickListener onItemClickListener) {
            super(v);
            layout = v;
            txtBeerName = v.findViewById(R.id.beer_name);
            txtBeerTagline = v.findViewById(R.id.beer_tagline);
            icon = v.findViewById(R.id.icon);

            txtBeerAbv = v.findViewById(R.id.beer_abv);
            txtBeerIbu = v.findViewById(R.id.beer_ibu);
            txtBeerEbc = v.findViewById(R.id.beer_ebc);
            txtBeerSrm = v.findViewById(R.id.beer_srm);

            this.onItemClickListener = onItemClickListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(beers.get(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Beer beer);
    }

    public void add(int position, Beer item) {
        beers.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        beers.remove(position);
        notifyItemRemoved(position);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public BeerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, onItemClickListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Beer beer = beers.get(position);

        holder.txtBeerName.setText(beer.getName());
        holder.txtBeerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove(position);
            }
        });

        holder.txtBeerTagline.setText(beer.getTagline());

        holder.txtBeerAbv.setText(beer.getAbv() + "%");
        holder.txtBeerIbu.setText(beer.getIbu() + "");
        holder.txtBeerEbc.setText(beer.getEbc() + "");
        holder.txtBeerSrm.setText(beer.getSrm() + "");

        Glide
                .with(this.context)
                .load(beer.getImageUrl())
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

}
