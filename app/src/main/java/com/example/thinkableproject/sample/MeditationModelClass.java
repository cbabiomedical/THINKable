package com.example.thinkableproject.sample;

public class MeditationModelClass {
    private int imageView;
    private String meditationName;
    private int isFavourite;
    private String meditation_url;

    public MeditationModelClass() {
    }
    public MeditationModelClass(int imageView, String meditationName, String meditation_url) {
        this.imageView = imageView;
        this.meditationName = meditationName;
        this.meditation_url = meditation_url;
    }
    public MeditationModelClass(int imageView, String meditationName, int isFavourite) {
        this.imageView = imageView;
        this.meditationName = meditationName;
        this.isFavourite = isFavourite;
    }



    public MeditationModelClass(int imageView, String meditationName, int isFavourite, String meditation_url) {
        this.imageView = imageView;
        this.meditationName = meditationName;
        this.isFavourite = isFavourite;
        this.meditation_url = meditation_url;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getMeditationName() {
        return meditationName;
    }

    public void setMeditationName(String meditationName) {
        this.meditationName = meditationName;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getMeditation_url() {
        return meditation_url;
    }

    public void setMeditation_url(String meditation_url) {
        this.meditation_url = meditation_url;
    }

    @Override
    public String toString() {
        return "MeditationModelClass{" +
                "imageView=" + imageView +
                ", meditationName='" + meditationName + '\'' +
                ", isFavourite=" + isFavourite +
                '}';
    }
}
