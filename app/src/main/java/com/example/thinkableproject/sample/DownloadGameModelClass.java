package com.example.thinkableproject.sample;

public class DownloadGameModelClass {
    private int imageView;
    private String gameName;

    public DownloadGameModelClass() {
    }

    public DownloadGameModelClass(int imageView, String gameName) {
        this.imageView = imageView;
        this.gameName = gameName;
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

    @Override
    public String toString() {
        return "DownloadGameModelClass{" +
                "imageView=" + imageView +
                ", gameName='" + gameName + '\'' +
                '}';
    }
}


