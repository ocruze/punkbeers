package com.ocruze.punkbeers.beer;

import com.google.gson.annotations.SerializedName;

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



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return id + ", " + name;
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
}
