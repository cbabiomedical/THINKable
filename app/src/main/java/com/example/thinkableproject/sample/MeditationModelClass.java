package com.example.thinkableproject.sample;

public class MeditationModelClass {
    private int imageView;
    private String meditationName;
    private int isFavourite;

    public MeditationModelClass(int imageView, String meditationName, int isFavourite) {
        this.imageView = imageView;
        this.meditationName = meditationName;
        this.isFavourite = isFavourite;
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

    @Override
    public String toString() {
        return "MeditationModelClass{" +
                "imageView=" + imageView +
                ", meditationName='" + meditationName + '\'' +
                ", isFavourite=" + isFavourite +
                '}';
    }
}
