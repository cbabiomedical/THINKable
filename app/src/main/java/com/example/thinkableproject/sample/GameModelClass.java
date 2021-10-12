package com.example.thinkableproject.sample;

public class GameModelClass {
    private int imageView;
    private String gameName;
    private int isFavourite;

    public GameModelClass(int imageView, String gameName, int isFavourite) {
        this.imageView = imageView;
        this.gameName = gameName;
        this.isFavourite = isFavourite;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int  getIsFavourite() {
        return isFavourite;
    }

    public void setFavourite(int favourite) {
        isFavourite = favourite;
    }
}
