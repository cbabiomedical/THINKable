package com.example.thinkableproject.sample;

public class FavouriteModelClass {
    public int imageView;
    public String favName;
    public int isFave;

    public FavouriteModelClass(int imageView, String favName, int isFave) {

        this.imageView = imageView;
        this.favName = favName;
        this.isFave = isFave;
    }

    public FavouriteModelClass() {
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getFavName() {
        return favName;
    }

    public void setFavName(String favName) {
        this.favName = favName;
    }

    public int getIsFave() {
        return isFave;
    }

    public void setIsFave(int isFave) {
        this.isFave = isFave;
    }

    @Override
    public String toString() {
        return "FavouriteModelClass{" +
                "imageView=" + imageView +
                ", favName='" + favName + '\'' +
                ", isFave=" + isFave +
                '}';
    }
}
