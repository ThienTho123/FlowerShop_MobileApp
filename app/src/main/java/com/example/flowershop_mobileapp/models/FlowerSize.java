package com.example.flowershop_mobileapp.models;

public class FlowerSize {
    private int flowerSizeID;
    private Flower flower; // Đã có class Flower
    private String sizeName;
    private float length;
    private float high;
    private float width;
    private float weight;
    private int stock;
    private double price;
    private double cost;
    private String status;
    private String preorderable;

    // Getter và Setter
    public int getFlowerSizeID() {
        return flowerSizeID;
    }

    public void setFlowerSizeID(int flowerSizeID) {
        this.flowerSizeID = flowerSizeID;
    }

    public Flower getFlower() {
        return flower;
    }

    public void setFlower(Flower flower) {
        this.flower = flower;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreorderable() {
        return preorderable;
    }

    public void setPreorderable(String preorderable) {
        this.preorderable = preorderable;
    }
}
