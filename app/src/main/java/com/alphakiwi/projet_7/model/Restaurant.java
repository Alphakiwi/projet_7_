package com.alphakiwi.projet_7.model;


import java.io.Serializable;
import java.util.Comparator;


public class Restaurant implements Serializable {


    public String id;
    public String name;
    public String address;

    public int distance;
    public int note;

    public Restaurant() { }



    public Restaurant( String name, String address, String id ) {
        this.name = name;
        this.address = address;
        this.id = id;


    }

    public Restaurant( String name, String address, String id, int distance, int note) {
        this.name = name;
        this.address = address;
        this.id = id;
        this.distance = distance;
        this.note = note;


    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address ; }
    public int getDistance() { return distance; }
    public int getNote() { return note ; }


    // --- SETTERS ---

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) { this.address = address; }
    public void setDistance(int distance) {
        this.distance = distance;
    }
    public void setNote(int note) {
        this.note = note;
    }



    public static class RestaurantAZComparator implements Comparator< Restaurant> {
        @Override
        public int compare( Restaurant left,  Restaurant right) {
            return left.name.compareTo(right.name);
        }
    }

    /**
     * Comparator to sort task from Z to A
     */
    public static class  RestaurantZAComparator implements Comparator< Restaurant> {
        @Override
        public int compare( Restaurant left,  Restaurant right) {
            return right.name.compareTo(left.name);
        }
    }

    public static class RestaurantMarksComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant left, Restaurant right) {
            return  (right.note- left.note);
        }
    }

    public static class RestaurantDistanceComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant left, Restaurant right) {
            return  (left.distance- right.distance);
        }
    }


}
