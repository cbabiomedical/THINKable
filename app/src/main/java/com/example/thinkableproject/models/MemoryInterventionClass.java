package com.example.thinkableproject.models;

public class MemoryInterventionClass {

    private int imageView;
    private String title;
    private String text;

    public MemoryInterventionClass() {
    }

    public MemoryInterventionClass(int imageView) {
        this.imageView = imageView;
    }

    public MemoryInterventionClass(int imageView, String title, String text) {
        this.imageView = imageView;
        this.title = title;
        this.text = text;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "MemoryInterventionClass{" +
                "imageView=" + imageView +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
