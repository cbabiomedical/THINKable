package com.example.thinkableproject.sample;

public class GameModelClass {
    private String gameImage;
    private String gameName;  //private variables for encapsulation
    private String gameId;
    private String fav;


    public GameModelClass() {
    }

    // Constructor
    public GameModelClass(String gameImage, String gameName, String gameId, String fav) {
        this.gameImage = gameImage;
        this.gameName = gameName;
        this.gameId = gameId;
        this.fav = fav;
    }

    public GameModelClass(String gameImage, String gameName) {
        this.gameImage = gameImage;
        this.gameName = gameName;
    }

    // public getters and setter method to access variables outside the class
    public String getGameImage() {
        return gameImage;
    }

    public void setGameImage(String imageView) {
        this.gameImage = imageView;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameId() {
        return gameId;
    }

    public void setId(String id) {
        this.gameId = gameId;
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
        return "GameModelClass{" +
                "gameImage='" + gameImage + '\'' +
                ", gameName='" + gameName + '\'' +
                ", gameId='" + gameId + '\'' +
                ", fav='" + fav + '\'' +
                '}';
    }
}
