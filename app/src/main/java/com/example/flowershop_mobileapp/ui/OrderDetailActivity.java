package com.example.flowershop_mobileapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView txtOrderID, txtCustomerName, txtTotalAmount, txtStatus, txtOrderDate,
            txtPhoneNumber, txtAddress, txtPaidAmount, txtRemainingAmount, txtShipID, txtShipperID,
            txtShipperName, txtNote, txtShipperNote, txtStartDelivery, txtEndDelivery, txtPaymentStatus;
    private TableLayout tableProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
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

        // Nhận orderID từ Intent
        int orderID = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderID != -1) {
            fetchOrderDetail(orderID);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy đơn hàng!", Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(OrderDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayOrderDetail(OrderDetail order) {
        txtOrderID.setText(String.valueOf(order.getOrderID()));
        txtCustomerName.setText(order.getCustomerName());
        txtTotalAmount.setText(order.getTotalAmount() + " đ");
        txtRemainingAmount.setText(order.getRemainingAmount() + " đ");
        txtStatus.setText(order.getStatus());
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

        String status = order.getStatus();
        if ("Return_to_shop".equals(status)) {
            status = "Trả về cửa hàng";
        } else if ("Delivered_Successfully".equals(status)) {
            status = "Giao hàng thành công";
        }
        txtStatus.setText(status);

        String paymentStatus = order.getPaymentStatus();
        if ("Yes".equals(paymentStatus)) {
            paymentStatus = "Đã thanh toán";
        } else if ("No".equals(paymentStatus)) {
            paymentStatus = "Chưa thanh toán";
        }
        txtPaymentStatus.setText(paymentStatus); // Hiển thị trạng thái đã dịch

        // Kiểm tra danh sách sản phẩm có null không
        List<Product> products = order.getProducts();
        if (products == null || products.isEmpty()) {
            Log.e("OrderDetailActivity", "Danh sách sản phẩm bị null hoặc rỗng!");
            return;
        }

        // Hiển thị danh sách sản phẩm trong đơn hàng
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

        tableProducts.addView(headerRow); // ✅ Thêm dòng tiêu đề vào bảng

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

            // ✅ Hiển thị đơn giá (tính lại)
            row.addView(createCell(product.getUnitPrice() + " đ"));

            // ✅ Hiển thị tổng giá (tính lại từ `unitPrice * quantity`)
            row.addView(createCell(product.getTotalPrice() + " đ"));

            // ✅ Hiển thị đã thanh toán (tránh null)
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
