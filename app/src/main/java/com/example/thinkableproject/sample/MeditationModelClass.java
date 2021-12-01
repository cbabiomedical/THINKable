package com.example.thinkableproject.sample;

public class MeditationModelClass {
    private String meditateName;
    private String meditateImage;
    private String mediateId;
    private String url;  //private variables for encapsulation
    private String fav;


    public MeditationModelClass() {
    }

    //Constructor

    public MeditationModelClass(String meditateName, String meditateImage, String mediateId, String url, String fav) {
        this.meditateImage = meditateImage;
        this.meditateName = meditateName;
        this.mediateId = mediateId;
        this.url = url;
        this.fav = fav;
    }

    public MeditationModelClass(String meditateName, String meditateImage, String mediateId, String url) {
        this.meditateName = meditateName;
        this.meditateImage = meditateImage;
        this.mediateId = mediateId;
        this.url = url;
    }

    //public getters and setters to access variables outside the class
    public String getMeditateImage() {
        return meditateImage;
    }

    public void setMeditateImage(String meditateImage) {
        this.meditateImage = meditateImage;
    }

    public String getMeditateName() {
        return meditateName;
    }

    public void setMeditateName(String gameName) {
        this.meditateName = gameName;
    }

    public String getMediateId() {
        return mediateId;
    }

    public void setMediateId(String mediateId) {
        this.mediateId = mediateId;
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

    //toString
    @Override
    public String toString() {
        return "MeditationModelClass{" +
                "meditateName='" + meditateName + '\'' +
                ", meditateImage='" + meditateImage + '\'' +
                ", mediateId='" + mediateId + '\'' +
                ", url='" + url + '\'' +
                ", fav='" + fav + '\'' +
                '}';
    }
}