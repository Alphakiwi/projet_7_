package com.alphakiwi.projet_7.model;


import java.io.Serializable;


public class Restaurant implements Serializable {


    public String id;
    public String name;
    public String address;

    public Restaurant() { }



    public Restaurant( String name, String address, String id ) {
        this.name = name;
        this.address = address;
        this.id = id;


    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address ; }


}
