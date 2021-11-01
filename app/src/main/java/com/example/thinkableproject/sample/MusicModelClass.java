package com.example.thinkableproject.sample;

public class MusicModelClass {
    private int imageView;
    private String songName;
    private String id;
    private String url;
    private String isFav;

    public MusicModelClass() {
    }

    public MusicModelClass(int imageView, String songName, String id, String url, String isFav) {
        this.imageView = imageView;
        this.songName = songName;
        this.id = id;
        this.url = url;
        this.isFav = isFav;
    }

    public MusicModelClass(int imageView, String songName, String id, String url) {
        this.imageView = imageView;
        this.songName = songName;
        this.id = id;
        this.url = url;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
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

    public String getIsFav() {
        return isFav;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }
}
