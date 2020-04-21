package com.ocruze.punkbeers.beer;

import androidx.annotation.NonNull;

public class Quantity {
    private double value;
    private String unit;

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @NonNull
    @Override
    public String toString() {
        return value + " " + unit;
    }
}