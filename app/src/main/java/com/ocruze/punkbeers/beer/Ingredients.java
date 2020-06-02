package com.ocruze.punkbeers.beer;

import java.util.List;

public class Ingredients {
    private List<Ingredient> malt;
    private List<Hop> hops;
    private String yeast;

    public List<Ingredient> getMalt() {
        return malt;
    }

    public List<Hop> getHops() {
        return hops;
    }

    public String getYeast() {
        return yeast;
    }

    class Ingredient {
        private String name;
        private Quantity amount;

        public String getName() {
            return name;
        }

        public Quantity getAmount() {
            return amount;
        }
    }

    class Hop extends Ingredient {
        private String add;
        private String attribute;

        public String getAdd() {
            return add;
        }

        public String getAttribute() {
            return attribute;
        }
    }
}