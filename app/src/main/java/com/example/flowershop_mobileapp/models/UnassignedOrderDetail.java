package com.example.flowershop_mobileapp.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class UnassignedOrderDetail implements Serializable {  // Implement Serializable
    private int orderID;
    private String date;
    private String isPaid;
    private BigDecimal hadPaid;
    private BigDecimal total;
    private String condition;
    private String name;
    private String phone;
    private String address;
    private String note;

    // Getters và Setters
    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getIsPaid() { return isPaid; }
    public void setIsPaid(String isPaid) { this.isPaid = isPaid; }

    public BigDecimal getHadPaid() { return hadPaid; }
    public void setHadPaid(BigDecimal hadPaid) { this.hadPaid = hadPaid; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
