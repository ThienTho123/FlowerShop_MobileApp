package com.example.flowershop_mobileapp.models;

public class ShippingRequest {
    private String note;

    public ShippingRequest(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
