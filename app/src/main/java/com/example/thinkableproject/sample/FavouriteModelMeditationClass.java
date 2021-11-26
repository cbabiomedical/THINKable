package com.example.thinkableproject.sample;

public class FavouriteModelMeditationClass {

    private String item_title;
    private String key_id;
    private String item_image;

    public FavouriteModelMeditationClass() {
    }

    public FavouriteModelMeditationClass(String item_title, String key_id, String item_image) {
        this.item_title = item_title;
        this.key_id = key_id;
        this.item_image = item_image;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }
}