package com.example.thinkableproject.sample;

public class UserPreferences {
    int imageUrl;
    String preferenceName;
    int preferenceNum;

    public UserPreferences() {
    }

    public UserPreferences(int imageUrl, String preferenceName, int preferenceNum) {
        this.imageUrl = imageUrl;
        this.preferenceName = preferenceName;
        this.preferenceNum = preferenceNum;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPreferenceName() {
        return preferenceName;
    }

    public void setPreferenceName(String preferenceName) {
        this.preferenceName = preferenceName;
    }

    public int getPreferenceNum() {
        return preferenceNum;
    }

    public void setPreferenceNum(int preferenceNum) {
        this.preferenceNum = preferenceNum;
    }
}
