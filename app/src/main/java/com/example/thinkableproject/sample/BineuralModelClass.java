package com.example.thinkableproject.sample;

public class BineuralModelClass {

    private int  bineuralImage;
    private String bineuralName;
    private int isFav;

    public BineuralModelClass(int bineuralImage, String bineuralName, int isFav) {
        this.bineuralImage = bineuralImage;
        this.bineuralName = bineuralName;
        this.isFav = isFav;
    }

    public int getBineuralImage() {
        return bineuralImage;
    }

    public void setBineuralImage(int bineuralImage) {
        this.bineuralImage = bineuralImage;
    }

    public String getBineuralName() {
        return bineuralName;
    }

    public void setBineuralName(String bineuralName) {
        this.bineuralName = bineuralName;
    }

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    @Override
    public String toString() {
        return "BineuralModelClass{" +
                "bineuralImage=" + bineuralImage +
                ", bineuralName='" + bineuralName + '\'' +
                ", isFav=" + isFav +
                '}';
    }
}
