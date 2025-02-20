package com.example.flowershop_mobileapp.models;
import com.example.flowershop_mobileapp.models.FlowerSize;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class OrderDetail {
    @SerializedName("orderdetailID")
    private int orderDetailID;

    @SerializedName("price")
    private BigDecimal price;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("flowerSize")
    private FlowerSize flowerSize;

    // Getter & Setter
    public int getOrderDetailID() { return orderDetailID; }
    public void setOrderDetailID(int orderDetailID) { this.orderDetailID = orderDetailID; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public FlowerSize getFlowerSize() { return flowerSize; }
    public void setFlowerSize(FlowerSize flowerSize) { this.flowerSize = flowerSize; }
}
