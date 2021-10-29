package com.example.thinkableproject.sample;

public class GameModelClass {
    private int imageView;
    private String gameName;
    private String id;
    private String fav;


    public GameModelClass() {
    }

    public GameModelClass(int imageView, String gameName, String id, String fav) {
        this.imageView = imageView;
        this.gameName = gameName;
        this.id = id;
        this.fav = fav;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }
}
