package com.example.flowershop_mobileapp.dto;

public class ShippingRequest {

    private String note;

    // Constructor
    public ShippingRequest(String note) {
        this.note = note;
    }

    // Getter and Setter
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}