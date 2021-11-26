package com.example.thinkableproject;


public class User {
    // Creating private variables to store user data
    // private variables for encapsulation
    private String userName, email, occupation, Gender, dob, preferences, suggestions, favourites, location, profile;
    private long coins = 25;

    // Default constructor
    public User() {

    }

    // Parameterized constructor to create user object
    public User(String userName, String email, String occupation, String gender, String dob, String preferences, String suggestions, String favourites, String location) {
        this.userName = userName;
        this.email = email;
        this.occupation = occupation;
        Gender = gender;
        this.dob = dob;
        this.preferences = preferences;
        this.suggestions = suggestions;
        this.favourites = favourites;
        this.location = location;
    }

    // Getters and Setters to access variable outside class
    public User(String preferences) {
        this.preferences = preferences;
    }

    public User(String userName, String email, String occupation, String gender, String dob, String preference, String suggestions, String favourites) {
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public String getFavourites() {
        return favourites;
    }

    public void setFavourites(String favourites) {
        this.favourites = favourites;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
