package com.ocruze.punkbeers.beer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Method {
    @SerializedName("mash_temp")
    private List<MashTemp> mashTemp;

    private Fermentation fermentation;

    private String twist;

    public MashTemp getMashTemp() {
        return mashTemp.get(0);
    }

    public Fermentation getFermentation() {
        return fermentation;
    }

    public String getTwist() {
        return twist;
    }

    class MashTemp {
        private Quantity temperature;
        private double duration;

        public Quantity getTemperature() {
            return temperature;
        }

        public double getDuration() {
            return duration;
        }
    }

    class Fermentation {
        private Quantity temperature;

        public Quantity getTemperature() {
            return temperature;
        }
    }
}
