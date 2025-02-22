package com.example.flowershop_mobileapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.flowershop_mobileapp.R;
import com.example.flowershop_mobileapp.models.OrderDetail;
import com.example.flowershop_mobileapp.models.Product;
import com.example.flowershop_mobileapp.network.ApiClient;
import com.example.flowershop_mobileapp.network.ApiService;
import com.example.flowershop_mobileapp.models.ShippingRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnassignedOrderDetailActivity extends AppCompatActivity {

    private TextView txtOrderID, txtCustomerName, txtTotalAmount, txtStatus, txtOrderDate,
            txtPhoneNumber, txtAddress, txtPaidAmount, txtRemainingAmount, txtPaymentStatus;
    private EditText edtNote;
    private Button btnAcceptOrder;
    private TableLayout tableProducts;
    private int orderID;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unassigned_order_detail);

        apiService = ApiClient.getApiService(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

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
        tableProducts = findViewById(R.id.tableProducts);

        orderID = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderID == -1) {
            Toast.makeText(this, "Không tìm thấy đơn hàng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadOrderDetails(orderID);

        btnAcceptOrder.setOnClickListener(v -> acceptOrder(orderID));
    }

    private void loadOrderDetails(int orderID) {
        apiService.getUnassignedOrdersDetail(orderID).enqueue(new Callback<OrderDetail>() {
            @Override
            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayOrderDetails(response.body());
                } else {
                    Toast.makeText(UnassignedOrderDetailActivity.this, "Lỗi tải chi tiết đơn hàng!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<OrderDetail> call, Throwable t) {
                Toast.makeText(UnassignedOrderDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayOrderDetails(OrderDetail orderDetail) {
        txtOrderID.setText(String.valueOf(orderDetail.getOrderID()));
        txtCustomerName.setText(orderDetail.getCustomerName());
        txtTotalAmount.setText(orderDetail.getTotalAmount().toString());
        txtStatus.setText(orderDetail.getStatus());
        txtOrderDate.setText(orderDetail.getOrderDate());
        txtPhoneNumber.setText(orderDetail.getPhoneNumber());
        txtAddress.setText(orderDetail.getAddress());
        txtPaidAmount.setText(orderDetail.getPaidAmount().toString());
        txtRemainingAmount.setText(orderDetail.getRemainingAmount().toString());
        txtPaymentStatus.setText(orderDetail.getPaymentStatus());

        populateProductTable(orderDetail);
    }

    private void populateProductTable(OrderDetail order) {
        txtOrderID.setText(String.valueOf(order.getOrderID()));
        txtCustomerName.setText(order.getCustomerName());
        txtTotalAmount.setText(order.getTotalAmount() + " đ");
        txtRemainingAmount.setText(order.getRemainingAmount() + " đ");
        txtStatus.setText(order.getStatus());
        txtOrderDate.setText(order.getOrderDate());
        txtPhoneNumber.setText(order.getPhoneNumber());
        txtAddress.setText(order.getAddress());
        txtPaidAmount.setText(order.getPaidAmount() + " đ");


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
        textView.setBackgroundColor(getResources().getColor(R.color.colorPinkSoft));
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        return textView;
    }

    private TextView createCell(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(12, 8, 12, 8);
        textView.setTextSize(14);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setBackgroundResource(R.drawable.cell_border);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        return textView;
    }

    private void acceptOrder(int orderID) {
        String note = edtNote.getText().toString();
        ShippingRequest request = new ShippingRequest(note);

        apiService.acceptOrder(orderID, request).enqueue(new Callback<Void>() {
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
