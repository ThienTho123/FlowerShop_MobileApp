package com.example.flowershop_mobileapp.models;

public class ShipperNoteImage {
    private String image;
    private String text;

    public ShipperNoteImage(String image, String text) {
        this.image = image;
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
