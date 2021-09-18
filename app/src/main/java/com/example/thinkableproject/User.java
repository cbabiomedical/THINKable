package com.example.thinkableproject;

import com.example.thinkableproject.sample.UserPreferences;

import java.util.prefs.Preferences;

public class User {
    private String userName, email,occupation,Gender, dob;


    String preferences;
    String suggestions;

    public User() {

    }

    public User(String userName, String email, String preferences, String suggestions) {
        this.userName = userName;
        this.email = email;
        this.preferences = preferences;
        this.suggestions = suggestions;
    }

    public User(String userName, String email, String occupation, String gender, String dob) {
        this.userName = userName;
        this.email = email;
        this.occupation = occupation;
        Gender = gender;
        this.dob = dob;

    }

    public User(String userName, String email, String occupation, String gender, String dob, String preferences, String suggestions) {
        this.userName = userName;
        this.email = email;
        this.occupation = occupation;
        Gender = gender;
        this.dob = dob;
        this.preferences = preferences;
        this.suggestions = suggestions;
    }

    public User(String preferences) {
        this.preferences = preferences;
    }

    public String  getPreferences() {
        return preferences;
    }

    public void setPreferences(String  preferences) {
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


}
