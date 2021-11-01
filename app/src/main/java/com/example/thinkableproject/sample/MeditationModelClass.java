package com.example.thinkableproject.sample;

public class MeditationModelClass {
    private String meditationName;
    private int imageView;
    private String id;
    private String fav;


    public MeditationModelClass() {
    }

    public MeditationModelClass( String meditationName,int imageView, String id, String fav) {
        this.imageView = imageView;
        this.meditationName = meditationName;
        this.id = id;
        this.fav = fav;
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

    public void setMeditationName(String gameName) {
        this.meditationName = gameName;
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
