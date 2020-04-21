package com.ocruze.punkbeers.beer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Beer {
    private int id;
    private String name;
    private String tagline;
    private String description;

    @SerializedName("image_url")
    private String imageUrl;

    private double abv;
    private double ibu;
    private double ebc;
    private double srm;

    private Quantity volume;

    @SerializedName("boil_volume")
    private Quantity boilVolume;

    private Method method;

    private Ingredients ingredients;

    @SerializedName("food_pairing")
    private List<String> foodPairing;

    @SerializedName("brewers_tips")
    private String brewersTips;

    @SerializedName("contributed_by")
    private String contributedBy;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getAbv() {
        return abv;
    }

    public double getIbu() {
        return ibu;
    }

    public double getEbc() {
        return ebc;
    }

    public double getSrm() {
        return srm;
    }

    public Quantity getVolume() {
        return volume;
    }

    public Quantity getBoilVolume() {
        return boilVolume;
    }


}


