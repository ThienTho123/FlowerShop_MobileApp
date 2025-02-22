package com.example.flowershop_mobileapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderDetail implements Serializable {
    @SerializedName("orderShippingDTO")
    private OrderData orderShippingDTO;

    @SerializedName("orderList")
    private OrderData orderList;

    // ✅ Hàm lấy dữ liệu từ orderShippingDTO hoặc orderList
    private OrderData getActiveOrder() {
        return orderShippingDTO != null ? orderShippingDTO : orderList;
    }

    public static class OrderData implements Serializable {
        @SerializedName("orderID")
        private int orderID;

        @SerializedName("name")
        private String customerName;

        @SerializedName("total")
        private BigDecimal totalAmount;

        @SerializedName("condition")
        private String status;

        @SerializedName("date")
        private String orderDate;

        @SerializedName("phone")
        private String phoneNumber;

        @SerializedName("address")
        private String address;

        @SerializedName("hadPaid")
        private BigDecimal paidAmount;

        @SerializedName("shippingID")
        private int shippingID;

        @SerializedName("shipperID")
        private int shipperID;

        @SerializedName("shipperName")
        private String shipperName;

        @SerializedName("note")
        private String note;

        @SerializedName("shipperNote")
        private String shipperNote;

        @SerializedName("shipStart")
        private String startDeliveryDate;

        @SerializedName("shipEnd")
        private String endDeliveryDate;

        @SerializedName("isPaid")
        private String paymentStatus;

        @SerializedName("flowerName")
        private String[] flowerNames;

        @SerializedName("sizeName")
        private String[] sizeNames;

        @SerializedName("length")
        private float[] lengths;

        @SerializedName("width")
        private float[] widths;

        @SerializedName("height")
        private float[] heights;

        @SerializedName("weight")
        private float[] weights;

        @SerializedName("quantity")
        private int[] quantities;

        @SerializedName("price")
        private BigDecimal[] prices;

        @SerializedName("paid")
        private BigDecimal[] paidAmounts;

        public List<Product> getProducts() {
            List<Product> products = new ArrayList<>();
            if (flowerNames == null || sizeNames == null || prices == null || quantities == null) {
                return products;
            }
            int count = flowerNames.length;
            for (int i = 0; i < count; i++) {
                Product product = new Product();
                product.setName(flowerNames[i]);
                product.setSize(sizeNames[i]);
                product.setQuantity(quantities[i]);

                if (prices[i] != null && quantities[i] > 0) {
                    product.setUnitPrice(prices[i].divide(BigDecimal.valueOf(quantities[i]), 2, BigDecimal.ROUND_HALF_UP));
                } else {
                    product.setUnitPrice(BigDecimal.ZERO);
                }

                product.setLength(i < lengths.length ? lengths[i] : 0);
                product.setWidth(i < widths.length ? widths[i] : 0);
                product.setHeight(i < heights.length ? heights[i] : 0);
                product.setWeight(i < weights.length ? weights[i] : 0);

                if (paidAmounts != null && paidAmounts.length > i) {
                    product.setPaidAmount(paidAmounts[i]);
                } else {
                    product.setPaidAmount(BigDecimal.ZERO);
                }

                products.add(product);
            }
            return products;
        }
    }

    public int getOrderID() { return getActiveOrder() != null ? getActiveOrder().orderID : 0; }
    public String getCustomerName() { return getActiveOrder() != null ? getActiveOrder().customerName : ""; }
    public BigDecimal getTotalAmount() { return getActiveOrder() != null ? getActiveOrder().totalAmount : BigDecimal.ZERO; }
    public String getStatus() { return getActiveOrder() != null ? getActiveOrder().status : ""; }
    public String getOrderDate() { return getActiveOrder() != null ? getActiveOrder().orderDate : ""; }
    public String getPhoneNumber() { return getActiveOrder() != null ? getActiveOrder().phoneNumber : ""; }
    public String getAddress() { return getActiveOrder() != null ? getActiveOrder().address : ""; }
    public BigDecimal getPaidAmount() { return getActiveOrder() != null ? getActiveOrder().paidAmount : BigDecimal.ZERO; }
    public int getShippingID() { return getActiveOrder() != null ? getActiveOrder().shippingID : 0; }
    public int getShipperID() { return getActiveOrder() != null ? getActiveOrder().shipperID : 0; }
    public String getShipperName() { return getActiveOrder() != null ? getActiveOrder().shipperName : ""; }
    public String getNote() { return getActiveOrder() != null ? getActiveOrder().note : ""; }
    public String getShipperNote() { return getActiveOrder() != null ? getActiveOrder().shipperNote : ""; }
    public String getStartDeliveryDate() { return getActiveOrder() != null ? getActiveOrder().startDeliveryDate : ""; }
    public String getEndDeliveryDate() { return getActiveOrder() != null ? getActiveOrder().endDeliveryDate : ""; }
    public String getPaymentStatus() { return getActiveOrder() != null ? getActiveOrder().paymentStatus : ""; }

    public String getOrderStatus() {
        return getActiveOrder() != null ? getActiveOrder().status : "";
    }

    public List<Product> getProducts() { return getActiveOrder() != null ? getActiveOrder().getProducts() : new ArrayList<>(); }

    public BigDecimal getRemainingAmount() {
        if (getActiveOrder() != null && getActiveOrder().totalAmount != null && getActiveOrder().paidAmount != null) {
            return getActiveOrder().totalAmount.subtract(getActiveOrder().paidAmount);
        }
        return BigDecimal.ZERO;
    }
}
