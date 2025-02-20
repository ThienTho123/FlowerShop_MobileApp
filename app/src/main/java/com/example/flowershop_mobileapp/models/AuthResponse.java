package com.example.flowershop_mobileapp.models;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("access_token") // ✅ Đảm bảo trùng với key API trả về
    private String accessToken;

    @SerializedName("refresh_token") // (Tuỳ chọn) Nếu cần refresh token
    private String refreshToken;

    @SerializedName("idAccount") // (Tuỳ chọn) Nếu cần ID tài khoản
    private int idAccount;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getIdAccount() {
        return idAccount;
    }
}
