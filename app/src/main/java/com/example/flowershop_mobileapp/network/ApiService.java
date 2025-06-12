package com.example.flowershop_mobileapp.network;
import com.example.flowershop_mobileapp.models.AuthResponse;
import com.example.flowershop_mobileapp.models.LoginRequest;
import com.example.flowershop_mobileapp.models.ChangePassword;

import com.example.flowershop_mobileapp.models.Order;
import com.example.flowershop_mobileapp.models.OrderDetail;
import com.example.flowershop_mobileapp.models.ShipperNoteImage;
import com.example.flowershop_mobileapp.models.ShippingRequest;
import com.example.flowershop_mobileapp.models.User;
import com.example.flowershop_mobileapp.ui.FlowerDetectResponse;

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

    @GET("/shipper/{id}")
    Call<OrderDetail> getUnassignedOrdersDetail(@Path("id") int orderID);

    // API nhận đơn hàng
    @POST("/shipper/{orderID}/receive")
    Call<Void> acceptOrder(@Path("orderID") int orderID, @Body ShippingRequest shippingRequest);


    @GET("shipperaccount/ordership/{orderid}")
    Call<OrderDetail> getOrderDetail(@Path("orderid") int orderID);


    @GET("/shipperaccount/haveship")
    Call<Map<String, List<Order>>> getPendingDeliveryOrders();

    @GET("/shipperaccount/haveship/{orderid}")
    Call<OrderDetail> getPendingOrderDetail(@Path("orderid") int orderID);
    @POST("/shipperaccount/haveship/{orderid}/start")
    Call<String> startDelivery(@Path("orderid") int orderID);

    @POST("/shipperaccount/haveship/{orderid}/success")
    Call<String> successDelivery(@Path("orderid") int orderID, @Body ShipperNoteImage requestBody);

    @POST("/shipperaccount/haveship/{orderid}/fail")
    Call<String> failDelivery(@Path("orderid") int orderID, @Body ShipperNoteImage requestBody);

// ========== FLOWER DETECTION API ==========

    /**
     * API nhận diện loài hoa từ hình ảnh
     * @param file MultipartBody.Part chứa file ảnh cần nhận diện
     * @return FlowerDetectResponse chứa kết quả nhận diện và danh sách sản phẩm gợi ý
     */
    @Multipart
    @POST("detect/upload")
    Call<FlowerDetectResponse> detectFlower(@Part MultipartBody.Part file);

    /**
     * API nhận diện loài hoa với token xác thực (nếu cần)
     * @param token Token xác thực người dùng
     * @param file MultipartBody.Part chứa file ảnh cần nhận diện
     * @return FlowerDetectResponse chứa kết quả nhận diện và danh sách sản phẩm gợi ý
     */
    @Multipart
    @POST("detect/upload")
    Call<FlowerDetectResponse> detectFlowerWithAuth(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file
    );

}