package com.example.thinkableproject.sample;

public class DownloadGameModelClass {
    private String gameImage;
    private String gameName;  //private variables for encapsulation

    public DownloadGameModelClass() {
    }

    //constructor
    public DownloadGameModelClass(String gameImage, String gameName) {
        this.gameImage = gameImage;
        this.gameName = gameName;
    }

    //public getters and setters
    public String getGameImage() {
        return gameImage;
    }

    public void setGameImage(String gameImage) {
        this.gameImage = gameImage;
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
                "gameImage='" + gameImage + '\'' +
                ", gameName='" + gameName + '\'' +
                '}';
    }
}


