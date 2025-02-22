package com.example.flowershop_mobileapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.flowershop_mobileapp.R;
import com.example.flowershop_mobileapp.models.OrderDetail;
import com.example.flowershop_mobileapp.models.Product;
import com.example.flowershop_mobileapp.network.ApiClient;
import com.example.flowershop_mobileapp.network.ApiService;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingOrdersDetailActivity extends AppCompatActivity {
    private TextView txtOrderID, txtCustomerName, txtTotalAmount, txtStatus, txtOrderDate,
            txtPhoneNumber, txtAddress, txtPaidAmount, txtRemainingAmount, txtShipID, txtShipperID,
            txtShipperName, txtNote, txtShipperNote, txtStartDelivery, txtEndDelivery, txtPaymentStatus;
    private TableLayout tableProducts;
    private Button btnStartDelivery, btnSuccessDelivery, btnFailDelivery;
    private int orderID;
    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed()); // 🔙 Xử lý quay lại

        // Ánh xạ view
        txtOrderID = findViewById(R.id.txtOrderID);
        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        txtStatus = findViewById(R.id.txtStatus);
        txtOrderDate = findViewById(R.id.txtOrderDate);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtAddress = findViewById(R.id.txtAddress);
        txtPaidAmount = findViewById(R.id.txtPaidAmount);
        txtRemainingAmount = findViewById(R.id.txtRemainingAmount);
        txtShipID = findViewById(R.id.txtShipID);
        txtShipperID = findViewById(R.id.txtShipperID);
        txtShipperName = findViewById(R.id.txtShipperName);
        txtNote = findViewById(R.id.txtNote);
        txtShipperNote = findViewById(R.id.txtShipperNote);
        txtStartDelivery = findViewById(R.id.txtStartDelivery);
        txtEndDelivery = findViewById(R.id.txtEndDelivery);
        txtPaymentStatus = findViewById(R.id.txtPaymentStatus);
        tableProducts = findViewById(R.id.tableProducts);
        btnStartDelivery = findViewById(R.id.btnStartDelivery);
        btnSuccessDelivery = findViewById(R.id.btnSuccessDelivery);
        btnFailDelivery = findViewById(R.id.btnFailDelivery);

        apiService = ApiClient.getApiService(this);

        // Nhận orderID từ Intent
        int orderID = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderID != -1) {
            fetchOrderDetail(orderID);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy đơn hàng!", Toast.LENGTH_SHORT).show();
        }

        btnStartDelivery.setOnClickListener(v -> startDelivery(orderID));
        btnSuccessDelivery.setOnClickListener(v -> successDelivery(orderID));
        btnFailDelivery.setOnClickListener(v -> failDelivery(orderID));
    }
    private void startDelivery(int orderID) {
        apiService.startDelivery(orderID).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(PendingOrdersDetailActivity.this, "Bắt đầu giao hàng!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(PendingOrdersDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void successDelivery(int orderID) {
        apiService.successDelivery(orderID).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(PendingOrdersDetailActivity.this, "Giao hàng thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(PendingOrdersDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void failDelivery(int orderID) {
        apiService.failDelivery(orderID).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(PendingOrdersDetailActivity.this, "Giao hàng thất bại!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(PendingOrdersDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchOrderDetail(int orderID) {
        ApiClient.getApiService(this).getOrderDetail(orderID).enqueue(new Callback<OrderDetail>() {
            @Override
            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Order detail: " + new Gson().toJson(response.body()));

                    if (response.body().getProducts() == null) {
                        Log.e("API_ERROR", "Danh sách sản phẩm bị null từ API!");
                    } else {
                        displayOrderDetail(response.body());
                    }
                } else {
                    Log.e("API_ERROR", "Lỗi tải chi tiết đơn hàng!");
                }
            }



            @Override
            public void onFailure(Call<OrderDetail> call, Throwable t) {
                Toast.makeText(PendingOrdersDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayOrderDetail(OrderDetail order) {
        txtOrderID.setText(String.valueOf(order.getOrderID()));
        txtCustomerName.setText(order.getCustomerName());
        txtTotalAmount.setText(order.getTotalAmount() + " đ");
        txtRemainingAmount.setText(order.getRemainingAmount() + " đ");

        // ✅ Chuyển trạng thái sang tiếng Việt trước khi hiển thị
        String status = order.getStatus();
        switch (status) {
            case "Cancel_is_Processing":
                status = "Hủy đang xử lý";
                break;
            case "Cancelled":
                status = "Đã hủy";
                break;
            case "In_Transit":
                status = "Đang vận chuyển";
                break;
            case "Shipper_Delivering":
                status = "Shipper đang giao hàng";
                break;
            case "First_Attempt_Failed":
                status = "Lần giao hàng đầu tiên thất bại";
                break;
            case "Second_Attempt_Failed":
                status = "Lần giao hàng thứ hai thất bại";
                break;
            case "Third_Attempt_Failed":
                status = "Lần giao hàng thứ ba thất bại";
                break;
            case "Delivered_Successfully":
                status = "Giao hàng thành công";
                break;
            case "Return_to_shop":
                status = "Trả về cửa hàng";
                break;
            case "Pending":
                status = "Đang chờ xử lý";
                break;
            case "Processing":
                status = "Đang xử lý";
                break;
            case "Prepare":
                status = "Chuẩn bị";
                break;
            default:
                status = "Không xác định";
                break;
        }
        txtStatus.setText(status); // ✅ Hiển thị trạng thái đã dịch

        txtOrderDate.setText(order.getOrderDate());
        txtPhoneNumber.setText(order.getPhoneNumber());
        txtAddress.setText(order.getAddress());
        txtPaidAmount.setText(order.getPaidAmount() + " đ");
        txtShipID.setText(String.valueOf(order.getShippingID()));
        txtShipperID.setText(String.valueOf(order.getShipperID()));
        txtShipperName.setText(order.getShipperName());
        txtNote.setText(order.getNote());
        txtShipperNote.setText(order.getShipperNote());
        txtStartDelivery.setText(order.getStartDeliveryDate());
        txtEndDelivery.setText(order.getEndDeliveryDate());

        btnStartDelivery.setVisibility(View.GONE);
        btnSuccessDelivery.setVisibility(View.GONE);
        btnFailDelivery.setVisibility(View.GONE);

        // ✅ Xử lý hiển thị nút theo trạng thái
        String orderStatus = order.getStatus();
        if ("In_Transit".equals(orderStatus)) {
            btnStartDelivery.setVisibility(View.VISIBLE);
        }
        if ("Shipper_Delivering".equals(orderStatus) ||
                "First_Attempt_Failed".equals(orderStatus) ||
                "Second_Attempt_Failed".equals(orderStatus) ||
                "Third_Attempt_Failed".equals(orderStatus)) {
            btnSuccessDelivery.setVisibility(View.VISIBLE);
            btnFailDelivery.setVisibility(View.VISIBLE);
        }

        // ✅ Chuyển trạng thái thanh toán sang tiếng Việt
        String paymentStatus = order.getPaymentStatus();
        if ("Yes".equals(paymentStatus)) {
            paymentStatus = "Đã thanh toán";
        } else if ("No".equals(paymentStatus)) {
            paymentStatus = "Chưa thanh toán";
        }
        txtPaymentStatus.setText(paymentStatus);

        // ✅ Kiểm tra danh sách sản phẩm
        List<Product> products = order.getProducts();
        if (products == null || products.isEmpty()) {
            Log.e("OrderDetailActivity", "Danh sách sản phẩm bị null hoặc rỗng!");
            return;
        }

        // ✅ Hiển thị danh sách sản phẩm trong đơn hàng
        tableProducts.removeAllViews();

        // ✅ Thêm dòng tiêu đề
        TableRow headerRow = new TableRow(this);
        headerRow.addView(createHeaderCell("STT"));
        headerRow.addView(createHeaderCell("Tên hoa"));
        headerRow.addView(createHeaderCell("Kích thước"));
        headerRow.addView(createHeaderCell("Dài x Rộng x Cao"));
        headerRow.addView(createHeaderCell("Khối lượng (kg)"));
        headerRow.addView(createHeaderCell("Số lượng"));
        headerRow.addView(createHeaderCell("Đơn giá (đ)"));
        headerRow.addView(createHeaderCell("Tổng (đ)"));
        headerRow.addView(createHeaderCell("Đã thanh toán (đ)"));

        tableProducts.addView(headerRow);

        // ✅ Hiển thị danh sách sản phẩm
        int index = 1;
        for (Product product : products) {
            TableRow row = new TableRow(this);
            row.addView(createCell(String.valueOf(index))); // STT
            row.addView(createCell(product.getName())); // Tên hoa
            row.addView(createCell(product.getSize())); // Size

            // ✅ Hiển thị kích thước (Dài x Rộng x Cao)
            String dimension = product.getLength() + " x " + product.getWidth() + " x " + product.getHeight();
            row.addView(createCell(dimension));

            // ✅ Hiển thị khối lượng (Weight)
            row.addView(createCell(String.valueOf(product.getWeight())));

            row.addView(createCell(String.valueOf(product.getQuantity()))); // Số lượng

            // ✅ Hiển thị đơn giá
            row.addView(createCell(product.getUnitPrice() + " đ"));

            // ✅ Hiển thị tổng giá
            row.addView(createCell(product.getTotalPrice() + " đ"));

            // ✅ Hiển thị đã thanh toán
            row.addView(createCell(product.getPaidAmount() + " đ"));

            tableProducts.addView(row);
            index++;
        }
    }

    private TextView createHeaderCell(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(12, 8, 12, 8);
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setBackgroundColor(getResources().getColor(R.color.colorPinkLight)); // ✅ Màu nền tiêu đề
        textView.setGravity(Gravity.CENTER);
        return textView;
    }





    private TextView createCell(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(12, 8, 12, 8);
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.drawable.cell_border);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
