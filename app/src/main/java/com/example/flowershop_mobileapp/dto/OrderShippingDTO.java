package com.example.flowershop_mobileapp.dto;

import java.math.BigDecimal;

public class OrderShippingDTO {

    private int orderID;
    private String name;
    private String address;
    private String phone;
    private String date;
    private String[] flowerName;
    private Integer[] quantity;
    private Float[] length;
    private Float[] height;
    private Float[] width;
    private Float[] weight;
    private String[] sizeName;
    private BigDecimal[] price;
    private BigDecimal[] paid;
    private boolean isPaid;
    private boolean hadPaid;
    private String condition;
    private String note;

    // Getters and Setters
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getFlowerName() {
        return flowerName;
    }

    public void setFlowerName(String[] flowerName) {
        this.flowerName = flowerName;
    }

    public Integer[] getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer[] quantity) {
        this.quantity = quantity;
    }

    public Float[] getLength() {
        return length;
    }

    public void setLength(Float[] length) {
        this.length = length;
    }

    public Float[] getHeight() {
        return height;
    }

    public void setHeight(Float[] height) {
        this.height = height;
    }

    public Float[] getWidth() {
        return width;
    }

    public void setWidth(Float[] width) {
        this.width = width;
    }

    public Float[] getWeight() {
        return weight;
    }

    public void setWeight(Float[] weight) {
        this.weight = weight;
    }

    public String[] getSizeName() {
        return sizeName;
    }

    public void setSizeName(String[] sizeName) {
        this.sizeName = sizeName;
    }

    public BigDecimal[] getPrice() {
        return price;
    }

    public void setPrice(BigDecimal[] price) {
        this.price = price;
    }

    public BigDecimal[] getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal[] paid) {
        this.paid = paid;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isHadPaid() {
        return hadPaid;
    }

    public void setHadPaid(boolean hadPaid) {
        this.hadPaid = hadPaid;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}