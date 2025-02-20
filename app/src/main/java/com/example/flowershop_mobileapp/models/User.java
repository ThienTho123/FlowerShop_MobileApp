package com.example.flowershop_mobileapp.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("accountID")
    private int accountId;

    @SerializedName("username")
    private String username;

    @SerializedName("name")
    private String fullName;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("email")
    private String email;

    @SerializedName("address")  // Thêm trường địa chỉ
    private String address;

    @SerializedName("avatar")
    private String avatarUrl;

    public User() {}

    public User(String fullName, String phoneNumber, String email, String address, String avatarUrl) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.username = username;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
