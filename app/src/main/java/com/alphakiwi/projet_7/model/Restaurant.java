package com.alphakiwi.projet_7.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Restaurant {


    private String id;
    private String name;

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

    // True if opened
    public boolean open;

    // Opening hour
    public int openingHour;

    // Closing hour
    public int closingHour;




    public Restaurant() { }

    public Restaurant(String id, String name, String urlPicture, String address, int rating, String phone, String website, float distance, ArrayList workmates, boolean open, int openingHour, int closingHour ) {
        this.id = id;
        this.name = name;
        this.urlPicture = urlPicture;
        this.address = address ;
        this.rating = rating ;
        this.phone = phone;
        this.website = website;
        this.distance =distance ;
        this.workmates = workmates ;
        this.open = open ;
        this.openingHour = openingHour ;
        this.closingHour = closingHour;


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
    public boolean getOpen() { return open; }
    public int getOpeningHour() { return openingHour; }
    public int getClosingHour() { return  closingHour; }


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
    public void setOpen(boolean open) { this.open =open ; }
    public void setOpeningHour(int openingHour) { this.openingHour = openingHour; }
    public void setClosingHour(int closingHour) { this.closingHour =closingHour ; }

}
