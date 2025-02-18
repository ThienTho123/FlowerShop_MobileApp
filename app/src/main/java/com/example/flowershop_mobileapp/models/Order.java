package com.example.flowershop_mobileapp.models;

import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("id")
    private int id;

    @SerializedName("customerName")
    private String customerName;

    @SerializedName("flowerType")
    private String flowerType;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("status")
    private String status;

    // Constructor, Getters and Setters
    public Order(int id, String customerName, String flowerType, int quantity, String status) {
        this.id = id;
        this.customerName = customerName;
        this.flowerType = flowerType;
        this.quantity = quantity;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFlowerType() {
        return flowerType;
    }

    public void setFlowerType(String flowerType) {
        this.flowerType = flowerType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
