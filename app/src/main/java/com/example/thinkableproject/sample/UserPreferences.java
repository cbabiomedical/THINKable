package com.example.thinkableproject.sample;

import com.example.thinkableproject.User;

public class UserPreferences {
    // private variables to store imageUrl and name of preference
    int imageUrl;
    String preferenceName;


    public UserPreferences() {  // Empty Constructor
    }

    public UserPreferences(int imageUrl, String preferenceName) {  // Parameterized constructor to create user preference object
        this.imageUrl = imageUrl;
        this.preferenceName = preferenceName;
    }

    // Getters and Setters to access variables outside class
    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPreferenceName() {
        return preferenceName;
    }

    public void setPreferenceName(String preferenceName) {
        this.preferenceName = preferenceName;
    }


}
