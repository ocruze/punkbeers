package com.ocruze.punkbeers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ocruze.punkbeers.beer.Beer;

import java.io.InputStream;
import java.util.List;

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.ViewHolder> {
    private List<Beer> beers;

    private OnItemListener onItemListener;

    private Context context;

    public BeerListAdapter(List<Beer> beers, OnItemListener onItemListener, Context context) {
        this.beers = beers;
        this.onItemListener = onItemListener;
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


        OnItemListener onItemListener;

        ViewHolder(View v, OnItemListener onItemListener) {
            super(v);
            layout = v;
            txtBeerName = v.findViewById(R.id.beer_name);
            txtBeerTagline = v.findViewById(R.id.beer_tagline);
            icon = v.findViewById(R.id.icon);

            txtBeerAbv = v.findViewById(R.id.beer_abv);
            txtBeerIbu = v.findViewById(R.id.beer_ibu);
            txtBeerEbc = v.findViewById(R.id.beer_ebc);
            txtBeerSrm = v.findViewById(R.id.beer_srm);

            this.onItemListener = onItemListener;
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener {
        void onItemClick(int position);
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
        ViewHolder vh = new ViewHolder(v, onItemListener);
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
        // holder.icon.setImageDrawable(getIconFromUrl(beer.getImageUrl()));

        // new DownloadImageFromInternet(holder.icon).execute(beer.getImageUrl());
        holder.txtBeerAbv.setText(beer.getAbv() + "%");
        holder.txtBeerIbu.setText(beer.getIbu() + "");
        holder.txtBeerEbc.setText(beer.getEbc() + "");
        holder.txtBeerSrm.setText(beer.getSrm() + "");

        //System.out.println(getApplicationContext());

        Glide
                .with(this.context)
                .load(beer.getImageUrl())
                .into(holder.icon);

        System.out.println("volume = " + beer.getVolume());
        System.out.println("boil volume = " + beer.getBoilVolume());
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }


    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                System.out.println("Error Message : " + e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
