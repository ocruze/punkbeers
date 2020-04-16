package com.ocruze.punkbeers;

import com.google.gson.annotations.SerializedName;

public class Beer {
    private int id;
    private String name;
    private String tagline;
    private String description;

    @SerializedName("image_url")
    private String imageUrl;


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
}
