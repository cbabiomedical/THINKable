package com.example.thinkableproject.sample;

public class DownloadGameModelClass {
private int imageView;
private String gameTitle;

    public DownloadGameModelClass() {
    }

    public DownloadGameModelClass(int imageView, String gameTitle) {
        this.imageView = imageView;
        this.gameTitle = gameTitle;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    @Override
    public String toString() {
        return "DownloadGameModelClass{" +
                "imageView=" + imageView +
                ", gameTitle='" + gameTitle + '\'' +
                '}';
    }
}

