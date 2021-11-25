package com.example.thinkableproject.sample;

public class GameModelClass {
    private String imageView;
    private String gameName;
    private String id;
    private String fav;


    public GameModelClass() {
    }

    public GameModelClass(String imageView, String gameName) {
        this.imageView = imageView;
        this.gameName = gameName;
    }

    public GameModelClass(String imageView, String gameName, String id, String fav) {
        this.imageView = imageView;
        this.gameName = gameName;
        this.id = id;
        this.fav = fav;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
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
