package com.example.flowershop_mobileapp.network;
import com.example.flowershop_mobileapp.models.AuthResponse;
import com.example.flowershop_mobileapp.models.LoginRequest;
import com.example.flowershop_mobileapp.models.Order;
import com.example.flowershop_mobileapp.models.ChangePassword;

import com.example.flowershop_mobileapp.dto.OrderShippingDTO;
import com.example.flowershop_mobileapp.dto.ShippingRequest;
import com.example.flowershop_mobileapp.models.OrderResponse;
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

    // Lấy tất cả đơn hàng chưa có shipping


    @GET("shipper") // Endpoint API
    Call<OrderResponse> getAllOrders();

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


    // Lấy thông tin chi tiết đơn hàng
    @GET("/shipper/{id}")
    Call<Map<String, Object>> getOrder(@Path("id") int orderId);

    // Chấp nhận đơn hàng (nhận shipping)
    @POST("/shipper/{orderID}/receive")
    Call<String> receiveOrder(@Path("orderID") int orderID, @Body ShippingRequest shippingRequest);
}