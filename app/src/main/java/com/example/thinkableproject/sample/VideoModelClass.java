package com.example.thinkableproject.sample;

public class VideoModelClass {

    private String videoId;
    private String videoName;
    private String videoImage;
    private String videoUrl;
    private String fav;

    public VideoModelClass() {
    }

    public VideoModelClass(String videoId, String videoName, String videoImage, String videoUrl) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoImage = videoImage;
        this.videoUrl = videoUrl;
    }

    public VideoModelClass(String videoId, String videoName, String videoImage, String videoUrl, String fav) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoImage = videoImage;
        this.videoUrl = videoUrl;
        this.fav = fav;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    @Override
    public String toString() {
        return "VideoModelClass{" +
                "videoId='" + videoId + '\'' +
                ", videoName='" + videoName + '\'' +
                ", videoImage='" + videoImage + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", fav='" + fav + '\'' +
                '}';
    }
}
