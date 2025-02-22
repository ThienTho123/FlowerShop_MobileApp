package com.example.flowershop_mobileapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.flowershop_mobileapp.R;
import com.example.flowershop_mobileapp.models.Order;
import com.example.flowershop_mobileapp.models.OrderDetail;
import com.example.flowershop_mobileapp.models.UnassignedOrderDetail;
import com.example.flowershop_mobileapp.network.ApiClient;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnassignedOrdersFragment extends Fragment {
    private TableLayout tableOrders;
    private SwipeRefreshLayout swipeRefresh;
    private static final int ROW_HEIGHT = 250;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unassigned_orders, container, false);

        tableOrders = view.findViewById(R.id.tableOrders);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> navigateBackToMain());

        fetchOrders();
        swipeRefresh.setOnRefreshListener(this::fetchOrders);

        return view;
    }

    private void fetchOrders() {
        swipeRefresh.setRefreshing(true);

        ApiClient.getApiService(getContext()).getUnassignedOrders().enqueue(new Callback<Map<String, List<Order>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Order>>> call, Response<Map<String, List<Order>>> response) {
                swipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orderList = response.body().get("orderList");
                    displayOrders(orderList);
                } else {
                    Toast.makeText(getContext(), "Lỗi tải danh sách đơn hàng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<Order>>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayOrders(List<Order> orders) {
        tableOrders.removeAllViews();

        // Thêm dòng tiêu đề
        TableRow headerRow = new TableRow(getContext());
        headerRow.addView(createHeaderCell("STT"));
        headerRow.addView(createHeaderCell("Mã Đơn"));
        headerRow.addView(createHeaderCell("Khách Hàng"));
        headerRow.addView(createHeaderCell("SĐT"));
        headerRow.addView(createHeaderCell("Địa Chỉ"));
        headerRow.addView(createHeaderCell("Trạng Thái"));

        tableOrders.addView(headerRow);

        int index = 1;
        for (Order order : orders) {
            TableRow row = new TableRow(getContext());

            TextView orderIDCell = createCell(String.valueOf(order.getOrderID()));
            orderIDCell.setTextColor(Color.BLUE);
            orderIDCell.setOnClickListener(v -> openOrderDetail(order.getOrderID()));

            row.addView(createCell(String.valueOf(index)));
            row.addView(orderIDCell);
            row.addView(createCell(order.getName()));
            row.addView(createCell(order.getPhoneNumber()));
            row.addView(createCell(order.getDeliveryAddress()));

            // ✅ Xử lý dịch trạng thái đơn hàng
            String status = order.getCondition();
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
            row.addView(createCell(status));

            tableOrders.addView(row);
            index++;
        }
    }

    private TextView createHeaderCell(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(12, 8, 12, 8);
        textView.setTextSize(16);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(getResources().getColor(R.color.colorPinkSoft));
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private void openOrderDetail(int orderID) {
        ApiClient.getApiService(getContext()).getUnassignedOrdersDetail(orderID).enqueue(new Callback<OrderDetail>() {
            @Override
            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(getContext(), UnassignedOrderDetailActivity.class);
                    intent.putExtra("ORDER_ID", orderID);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Lỗi tải chi tiết đơn hàng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderDetail> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private TextView createCell(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(12, 8, 12, 8);
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.drawable.cell_border);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> navigateBackToMain());
    }

    private void navigateBackToMain() {
        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            getParentFragmentManager().popBackStack();
        } else {
            requireActivity().onBackPressed();
        }
    }
}
