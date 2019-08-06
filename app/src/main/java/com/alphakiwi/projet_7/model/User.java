package com.alphakiwi.projet_7.model;

import androidx.annotation.Nullable;



public class User {

    private String uid;
    private String username;
    private boolean notification;

    @Nullable
    private String urlPicture;

    @Nullable
    private String resto ;


    public User() { }

    public User(String uid, String username, String urlPicture,String resto ,boolean notification) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.resto = resto;
        this.notification= notification;

    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getResto() { return resto; }
    public boolean getNotification() { return notification; }


    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setResto(String resto) { this.resto = resto; }
    public void setNotification(boolean notification) { this.notification = notification; }

}
