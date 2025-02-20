package com.example.flowershop_mobileapp.models;

import java.util.List;

public class OrderResponse {
    private List<Order> orderList;

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
