package com.example.flowershop_mobileapp.models;

import com.google.gson.annotations.SerializedName;

public class OrderDetailResponse {
    @SerializedName("orderList")
    private OrderDetail orderDetail;

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }
}
