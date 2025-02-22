package com.example.flowershop_mobileapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.flowershop_mobileapp.R;
import com.example.flowershop_mobileapp.models.ShippingRequest;
import com.example.flowershop_mobileapp.models.UnassignedOrderDetail;
import com.example.flowershop_mobileapp.network.ApiClient;
import com.example.flowershop_mobileapp.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnassignedOrderDetailActivity extends AppCompatActivity {
    private TextView txtOrderID, txtCustomerName, txtTotalAmount, txtStatus, txtOrderDate,
            txtPhoneNumber, txtAddress, txtPaidAmount, txtRemainingAmount, txtPaymentStatus;
    private EditText edtNote;
    private Button btnAcceptOrder;
    private int orderId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unassigned_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        txtOrderID = findViewById(R.id.txtOrderID);
        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        txtStatus = findViewById(R.id.txtStatus);
        txtOrderDate = findViewById(R.id.txtOrderDate);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtAddress = findViewById(R.id.txtAddress);
        txtPaidAmount = findViewById(R.id.txtPaidAmount);
        txtRemainingAmount = findViewById(R.id.txtRemainingAmount);
        txtPaymentStatus = findViewById(R.id.txtPaymentStatus);
        edtNote = findViewById(R.id.edtNote);
        btnAcceptOrder = findViewById(R.id.btnAcceptOrder);
        apiService = ApiClient.getApiService(this);

        orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderId != -1) {
            fetchOrderDetail(orderId);
        }

        btnAcceptOrder.setOnClickListener(view -> acceptOrder(orderId));
    }

    private void fetchOrderDetail(int orderID) {
        apiService.getUnassignedOrdersDetail(orderID).enqueue(new Callback<UnassignedOrderDetail>() {
            @Override
            public void onResponse(Call<UnassignedOrderDetail> call, Response<UnassignedOrderDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UnassignedOrderDetail order = response.body();
                    txtOrderID.setText(String.valueOf(order.getOrderID()));
                    txtCustomerName.setText(order.getName());
                    txtTotalAmount.setText(order.getTotal() + " đ");
                    txtStatus.setText(order.getCondition());
                    txtOrderDate.setText(order.getDate());
                    txtPhoneNumber.setText(order.getPhone());
                    txtAddress.setText(order.getAddress());
                    txtPaidAmount.setText(order.getHadPaid() + " đ");
                    txtRemainingAmount.setText("0 đ");
                    txtPaymentStatus.setText(order.getIsPaid().equals("Yes") ? "Đã thanh toán" : "Chưa thanh toán");
                }
            }

            @Override
            public void onFailure(Call<UnassignedOrderDetail> call, Throwable t) {
                Toast.makeText(UnassignedOrderDetailActivity.this, "Lỗi tải chi tiết đơn hàng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void acceptOrder(int orderID) {
        String note = edtNote.getText().toString();
        apiService.receiveOrder(orderID, new ShippingRequest(note)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UnassignedOrderDetailActivity.this, "Nhận đơn thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UnassignedOrderDetailActivity.this, "Lỗi nhận đơn!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UnassignedOrderDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
