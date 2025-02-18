package com.example.flowershop_mobileapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flowershop_mobileapp.models.Order;
import com.example.flowershop_mobileapp.network.ApiClient;
import com.example.flowershop_mobileapp.network.ApiService;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ApiService apiService;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiClient.getApiService();
        fetchOrders();
    }


    private void fetchOrders() {
        // Gọi phương thức getAllOrders() thay vì getOrders()
        apiService.getAllOrders().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Lấy danh sách đơn hàng từ response
                    Map<String, Object> orderMap = response.body();
                    List<Order> orders = (List<Order>) orderMap.get("orderList");

                    // Đặt adapter cho RecyclerView
                    orderAdapter = new OrderAdapter(orders);
                    recyclerView.setAdapter(orderAdapter);

                    // Log thông tin đơn hàng với các phương thức getter
                    for (Order order : orders) {
                        Log.d("API", "ID: " + order.getId());
                        Log.d("API", "Customer Name: " + order.getCustomerName());
                        Log.d("API", "Flower Type: " + order.getFlowerType());
                        Log.d("API", "Quantity: " + order.getQuantity());
                        Log.d("API", "Status: " + order.getStatus());
                    }
                } else {
                    Log.e("API", "Lỗi response: " + response.message());
                    Toast.makeText(MainActivity.this, "Lỗi tải đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("API", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
