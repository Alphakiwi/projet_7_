package com.alphakiwi.projet_7.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

public class Restaurant implements Serializable {


    private String id;
    public String name;

    @Nullable
    private String urlPicture;

    // Address
    public String address;

    // Rating
    public int rating ;

    // Phone number
    public String phone ;

    // Website
    public String website;


    // Distance
    public float distance;

    // Workmates coming to this restaurant
    public ArrayList workmates;


    // Opening hour
    public int openingHour;





    public Restaurant() { }

    public Restaurant(String id, String name, String urlPicture, String address, int rating, String phone, String website, float distance, ArrayList workmates, int openingHour ) {
        this.id = id;
        this.name = name;
        this.urlPicture = urlPicture;
        this.address = address ;
        this.rating = rating ;
        this.phone = phone;
        this.website = website;
        this.distance =distance ;
        this.workmates = workmates ;
        this.openingHour = openingHour ;


    }

    public Restaurant( String name, String address ) {
        this.name = name;
        this.address = address;


    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getName() { return name; }
    public String getUrlPicture() { return urlPicture; }
    public String getAddress() { return address ; }
    public int getRating (){ return rating ; }
    public String getPhone() { return phone; }
    public String getWebsite() { return website ; }
    public float getDistance() { return distance ; }
    public ArrayList getWorkmates() { return workmates ; }
    public int getOpeningHour() { return openingHour; }


    // --- SETTERS ---
    public void setUsername(String username) { this.name = name; }
    public void setUid(String uid) { this.id = id; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setAddress(String address) { this.address = address; }
    public void setRating(int rating) { this.rating = rating; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setWebsite(String website) { this.website = website; }
    public void setDistance(float distance) { this.distance =distance ; }
    public void setWorkmates(ArrayList workmates ) { this.workmates = workmates; }
    public void setOpeningHour(int openingHour) { this.openingHour = openingHour; }

}
