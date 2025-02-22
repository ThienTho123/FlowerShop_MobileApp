package com.example.flowershop_mobileapp.models;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class Product {
    @SerializedName("name")
    private String name;

    @SerializedName("size")
    private String size;

    @SerializedName("length")
    private float length;

    @SerializedName("width")
    private float width;

    @SerializedName("height")
    private float height;

    @SerializedName("weight")
    private float weight;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("price")  // ✅ Giá từ API
    private BigDecimal totalPriceFromAPI;

    @SerializedName("paid")  // ✅ Đã thanh toán từ API
    private BigDecimal paidAmount;

    private BigDecimal unitPrice; // ✅ Đơn giá (tính lại)
    private BigDecimal totalPrice; // ✅ Tổng giá (tính lại)

    // **Getters & Setters**
    public String getName() { return name; }
    public String getSize() { return size; }
    public float getLength() { return length; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getWeight() { return weight; }
    public int getQuantity() { return quantity; }

    public BigDecimal getPaidAmount() {
        return paidAmount != null ? paidAmount : BigDecimal.ZERO; // ✅ Tránh lỗi null
    }

    // ✅ Tính đơn giá = Tổng giá từ API / Số lượng
    public BigDecimal getUnitPrice() {
        if (unitPrice == null) {
            if (quantity > 0 && totalPriceFromAPI != null) {
                unitPrice = totalPriceFromAPI.divide(BigDecimal.valueOf(quantity), 2, BigDecimal.ROUND_HALF_UP);
            } else {
                unitPrice = BigDecimal.ZERO;
            }
        }
        return unitPrice;
    }

    // ✅ Thêm phương thức `setUnitPrice()` để sửa lỗi
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    // ✅ Tính lại tổng giá = `unitPrice * quantity`
    public BigDecimal getTotalPrice() {
        if (totalPrice == null) {
            totalPrice = getUnitPrice().multiply(BigDecimal.valueOf(quantity));
        }
        return totalPrice;
    }

    public void setName(String name) { this.name = name; }
    public void setSize(String size) { this.size = size; }
    public void setLength(float length) { this.length = length; }
    public void setWidth(float width) { this.width = width; }
    public void setHeight(float height) { this.height = height; }
    public void setWeight(float weight) { this.weight = weight; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPriceFromAPI = totalPrice; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
}
