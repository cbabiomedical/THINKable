package com.example.thinkableproject.sample;

public class ModelClass {
    private int imageView;
    private String name;

    public ModelClass() {
    }

    public ModelClass(int imageView, String name) {
        this.imageView = imageView;
        this.name = name;
    }

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
