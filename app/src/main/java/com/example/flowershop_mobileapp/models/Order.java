package com.example.flowershop_mobileapp.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Order {
    private int orderID;
    private String name;
    private String phoneNumber;
    private String deliveryAddress;
    private List<Integer> date;
    private String condition;

    public Order(int orderID, String name, String phoneNumber, String deliveryAddress, List<Integer> date, String condition) {
        this.orderID = orderID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.date = date;
        this.condition = condition;
    }

    public int getOrderID() { return orderID; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getCondition() { return condition; }

    // Chuyển danh sách số nguyên thành ngày/tháng/năm
    public String getFormattedDate() {
        if (date == null || date.size() < 6) {
            return "N/A"; // Dữ liệu không hợp lệ
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(date.get(0), date.get(1) - 1, date.get(2), date.get(3), date.get(4), date.get(5));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}
