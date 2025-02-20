package com.example.flowershop_mobileapp.models;
import com.example.flowershop_mobileapp.models.FlowerSize;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.List;

public class Order {
    @SerializedName("orderID")  // Đảm bảo Retrofit map đúng dữ liệu
    private int orderID;

    @SerializedName("name")
    private String customerName;

    @SerializedName("totalAmount")
    private BigDecimal totalAmount;

    @SerializedName("orderDetails")
    private List<OrderDetail> orderDetails;

    // Getter & Setter
    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public List<OrderDetail> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(List<OrderDetail> orderDetails) { this.orderDetails = orderDetails; }
}
