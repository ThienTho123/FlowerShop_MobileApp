package com.example.flowershop_mobileapp.network;
import com.example.flowershop_mobileapp.models.AuthResponse;
import com.example.flowershop_mobileapp.models.LoginRequest;
import com.example.flowershop_mobileapp.models.ChangePassword;

import com.example.flowershop_mobileapp.models.ShippingRequest;
import com.example.flowershop_mobileapp.models.Order;
import com.example.flowershop_mobileapp.models.OrderDetail;
import com.example.flowershop_mobileapp.models.UnassignedOrderDetail;
import com.example.flowershop_mobileapp.models.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/v1/auth/authenticate") // ⚡ Cập nhật đường dẫn đúng
    Call<AuthResponse> login(@Body LoginRequest loginRequest);
    @GET("shipperaccount")
    Call<User> getUserProfile(@Header("Authorization") String token);
    @Multipart
    @POST("api/v1/upload")
    Call<Map<String, Object>> uploadImage(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file
    );

    @PUT("shipperaccount/updateinfo")
    Call<Void> updateUserProfile(
            @Header("Authorization") String token,
            @Body User user
    );
    @PUT("/shipperaccount/changepassword")
    Call<Void> changePassword(@Body ChangePassword changePassword);

    @GET("shipperaccount/ordership")
    Call<Map<String, List<Order>>> getDeliveredOrders();
    @GET("/shipper")
    Call<Map<String, List<Order>>> getUnassignedOrders();

    @GET("shipper/{id}")
    Call<UnassignedOrderDetail> getUnassignedOrdersDetail(@Path("id") int orderUnassignedId);

    // API nhận đơn hàng
    @POST("shipper/{id}/receive")
    Call<Void> receiveOrder(@Path("id") int orderId, @Body ShippingRequest request);


    @GET("shipperaccount/ordership/{orderid}")
    Call<OrderDetail> getOrderDetail(@Path("orderid") int orderID);

}