package com.example.thinkableproject;

public class User {
    private String userName, email,occupation,Gender, dob;

    public User() {
    }

    public User(String userName, String email, String occupation, String gender, String dob) {
        this.userName = userName;
        this.email = email;
        this.occupation = occupation;
        Gender = gender;
        this.dob = dob;

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
