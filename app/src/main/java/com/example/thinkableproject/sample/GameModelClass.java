package com.example.thinkableproject.sample;

public class GameModelClass {
    private String gameImage;
    private String gameName;
    private String gameId;
    private String fav;


    public GameModelClass() {
    }


    public GameModelClass(String gameImage, String gameName, String gameId, String fav) {
        this.gameImage = gameImage;
        this.gameName = gameName;
        this.gameId = gameId;
        this.fav = fav;
    }

    public GameModelClass(String gameImage, String gameName) {
        this.gameImage=gameImage;
        this.gameName=gameName;
    }

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
