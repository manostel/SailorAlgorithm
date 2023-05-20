package com.example.tspjavapp;

public class City {

    private String name;
    private int x;
    private int y;

    public City(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int distanceTo(City city) {
        int xDistance = Math.abs(getX() - city.getX());
        int yDistance = Math.abs(getY() - city.getY());
        return (int) Math.sqrt((xDistance*xDistance) + (yDistance*yDistance));
    }

    @Override
    public String toString() {
        return getName() + ": x = " + getX() + ", y = " + getY();
    }
}
