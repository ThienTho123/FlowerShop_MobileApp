package com.example.flowershop_mobileapp.network;
import com.example.flowershop_mobileapp.models.Order;

import com.example.flowershop_mobileapp.dto.OrderShippingDTO;
import com.example.flowershop_mobileapp.dto.ShippingRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;

public interface ApiService {

    // Lấy tất cả đơn hàng chưa có shipping
    @GET("/shipper")
    Call<Map<String, Object>> getAllOrders();

    // Lấy thông tin chi tiết đơn hàng
    @GET("/shipper/{id}")
    Call<Map<String, Object>> getOrder(@Path("id") int orderId);

    // Chấp nhận đơn hàng (nhận shipping)
    @POST("/shipper/{orderID}/receive")
    Call<String> receiveOrder(@Path("orderID") int orderID, @Body ShippingRequest shippingRequest);
}