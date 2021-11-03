package com.example.thinkableproject.sample;

public class MeditationModelClass {
    private String meditationName;
    private int imageView;
    private String id;
    private String url;
    private String fav;


    public MeditationModelClass() {
    }

    public MeditationModelClass( String meditationName,int imageView, String id, String url, String fav) {
        this.imageView = imageView;
        this.meditationName = meditationName;
        this.id = id;
        this.url=url;
        this.fav = fav;
    }

    public MeditationModelClass(String meditationName, int imageView, String id, String url) {
        this.meditationName = meditationName;
        this.imageView = imageView;
        this.id = id;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }
}
