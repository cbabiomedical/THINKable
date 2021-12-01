package com.example.thinkableproject.sample;

public class SuggestionsModel {
    private int imageView;  //private variables
    private String name;

    //Constructor
    public SuggestionsModel(int imageView, String name) {
        this.imageView = imageView;
        this.name = name;
    }
    //public getters and setters to access variables outside the class
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

    //toString
    @Override
    public String toString() {
        return "SuggestionsModel{" +
                "imageView=" + imageView +
                ", name='" + name + '\'' +
                '}';
    }
}
