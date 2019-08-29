package com.alphakiwi.projet_7.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class Restaurant implements Serializable {


    private String id;
    public String name;


    // Address
    public String address;






    public Restaurant() { }



    public Restaurant( String name, String address ) {
        this.name = name;
        this.address = address;


    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address ; }


    // --- SETTERS ---
    public void setUsername(String username) { this.name = name; }
    public void setUid(String uid) { this.id = id; }
    public void setAddress(String address) { this.address = address; }

}
