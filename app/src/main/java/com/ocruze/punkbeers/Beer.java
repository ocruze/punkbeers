package com.ocruze.punkbeers;

public class Beer {
    private int id;
    private String name;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return id + ", " + name;
    }
}
