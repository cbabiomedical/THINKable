package com.example.thinkableproject.sample;

public class ModelClass {
    private int imageView;  // private variables to store imageUrl and name of preference
    private String name;

    // Empty Constructor
    public ModelClass() {
    }

    // Parameterized constructor to create user preference object
    public ModelClass(int imageView, String name) {
        this.imageView = imageView;
        this.name = name;
    }
    // Getters and Setters to access variables outside class
    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
