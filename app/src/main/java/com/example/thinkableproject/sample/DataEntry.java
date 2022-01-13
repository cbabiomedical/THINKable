package com.example.thinkableproject.sample;

public class DataEntry {
    private float xVal;
    private float yVal;

    public DataEntry() {
    }

    public DataEntry(float xVal, float yVal) {
        this.xVal = xVal;
        this.yVal = yVal;
    }

    public float getxVal() {
        return xVal;
    }

    public void setxVal(float xVal) {
        this.xVal = xVal;
    }

    public float getyVal() {
        return yVal;
    }

    public void setyVal(float yVal) {
        this.yVal = yVal;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "xVal=" + xVal +
                ", yVal=" + yVal +
                '}';
    }
}
