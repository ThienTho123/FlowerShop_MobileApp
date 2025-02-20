package com.example.flowershop_mobileapp.models;

public class Flower {
    private int flowerID;
    private String name;
    private String description;
    private String image;
    private String languageOfFlowers;

    // Getter và Setter
    public int getFlowerID() {
        return flowerID;
    }

    public void setFlowerID(int flowerID) {
        this.flowerID = flowerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLanguageOfFlowers() {
        return languageOfFlowers;
    }

    public void setLanguageOfFlowers(String languageOfFlowers) {
        this.languageOfFlowers = languageOfFlowers;
    }
}
