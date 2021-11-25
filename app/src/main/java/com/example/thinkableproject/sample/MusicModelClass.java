package com.example.thinkableproject.sample;

public class MusicModelClass {
    private String imageUrl;
    private String songTitle1;
    private String id;
    private String url;
    private String isFav;

    public MusicModelClass() {
    }

    public MusicModelClass(String imageUrl, String songTitle1, String id, String url, String isFav) {
        this.imageUrl = imageUrl;
        this.songTitle1 = songTitle1;
        this.id = id;
        this.url = url;
        this.isFav = isFav;
    }

    public MusicModelClass(String imageUrl, String songTitle1, String id, String url) {
        this.imageUrl = imageUrl;
        this.songTitle1 = songTitle1;
        this.id = id;
        this.url = url;
    }

    public MusicModelClass(String imageUrl, String songTitle1) {
        this.imageUrl=imageUrl;
        this.songTitle1=songTitle1;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSongTitle1() {
        return songTitle1;
    }

    public void setSongTitle1(String songTitle1) {
        this.songTitle1 = songTitle1;
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

    @Override
    public String toString() {
        return "MusicModelClass{" +
                "imageView=" + imageUrl +
                ", songName='" + songTitle1 + '\'' +
                ", id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", isFav='" + isFav + '\'' +
                '}';
    }
}
