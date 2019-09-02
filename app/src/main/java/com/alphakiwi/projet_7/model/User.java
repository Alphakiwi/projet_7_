package com.alphakiwi.projet_7.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class User {

    private String uid;
    private String username;
    private boolean notification;

    @Nullable
    private String urlPicture;

    @Nullable
    private Restaurant resto ;


    private ArrayList<String> restoLike;


    public User() { }

    public User(String uid, String username, String urlPicture,Restaurant resto ,boolean notification, ArrayList<String> restoLike) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.resto = resto;
        this.notification= notification;
        this.restoLike = restoLike;

    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public Restaurant getResto() { return resto; }
    public boolean getNotification() { return notification; }
    public ArrayList<String> getRestoLike () {return restoLike;}


    // --- SETTERS ---
    public void setResto(Restaurant resto) { this.resto = resto; }

}
