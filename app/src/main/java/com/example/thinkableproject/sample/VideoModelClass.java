package com.example.thinkableproject.sample;

public class VideoModelClass {

    private String id;
    private String name;
    private int imageUrl;
    private int videoUrl;
    private String isFav;

    public VideoModelClass() {
    }

    public VideoModelClass(String id, String name, int imageUrl, int videoUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    public VideoModelClass(String id, String name, int imageUrl, int videoUrl, String isFav) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.isFav = isFav;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(int videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getIsFav() {
        return isFav;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }

    @Override
    public String toString() {
        return "VideoModelClass{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl=" + imageUrl +
                ", videoUrl=" + videoUrl +
                ", isFav='" + isFav + '\'' +
                '}';
    }
}
