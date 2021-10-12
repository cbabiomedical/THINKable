package com.example.thinkableproject.sample;

public class FavouriteModelClass {
    private int imageView;
    private String favoriteName;
    private int isFavourite;

    public FavouriteModelClass(int imageView, String favoriteName, int isFavourite) {
        this.imageView = imageView;
        this.favoriteName = favoriteName;
        this.isFavourite = isFavourite;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    @Override
    public String toString() {
        return "FavouriteModelClass{" +
                "imageView=" + imageView +
                ", favoriteName='" + favoriteName + '\'' +
                ", isFavourite=" + isFavourite +
                '}';
    }
}
