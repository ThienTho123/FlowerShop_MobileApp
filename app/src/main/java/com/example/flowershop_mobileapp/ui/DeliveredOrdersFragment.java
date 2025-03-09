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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.flowershop_mobileapp.MainActivity;
import com.example.flowershop_mobileapp.R;
import com.example.flowershop_mobileapp.network.ApiClient;
import com.example.flowershop_mobileapp.models.Order;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveredOrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private SwipeRefreshLayout swipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivered_orders, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewOrders);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter();
        recyclerView.setAdapter(orderAdapter);

        swipeRefresh.setOnRefreshListener(this::fetchOrders);
        fetchOrders();

        return view;
    }

    private void fetchOrders() {
        swipeRefresh.setRefreshing(true);

        ApiClient.getApiService(getContext()).getDeliveredOrders().enqueue(new Callback<Map<String, List<Order>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Order>>> call, Response<Map<String, List<Order>>> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body().get("orderList");
                    orderAdapter.setOrders(orders);
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

//    private void displayOrders(List<Order> orders) {
//        tableOrders.removeAllViews();
//
//        // ✅ Thêm dòng tiêu đề
//        TableRow headerRow = new TableRow(getContext());
//        headerRow.addView(createHeaderCell("STT"));
//        headerRow.addView(createHeaderCell("Mã Đơn"));
//        headerRow.addView(createHeaderCell("Khách Hàng"));
//        headerRow.addView(createHeaderCell("SĐT"));
//        headerRow.addView(createHeaderCell("Địa Chỉ"));
//        headerRow.addView(createHeaderCell("Ngày Nhận"));
//        headerRow.addView(createHeaderCell("Trạng Thái"));
//
//        tableOrders.addView(headerRow); // ✅ Thêm tiêu đề vào bảng
//
//        int index = 1;
//        for (Order order : orders) {
//            TableRow row = new TableRow(getContext());
//
//            // Mã đơn hàng (có thể bấm vào)
//            TextView orderIDCell = createCell(String.valueOf(order.getOrderID()));
//            orderIDCell.setTextColor(Color.BLUE);
//            orderIDCell.setOnClickListener(v -> openOrderDetail(order.getOrderID()));
//
//            row.addView(createCell(String.valueOf(index)));  // STT
//            row.addView(orderIDCell);  // Mã đơn
//            row.addView(createCell(order.getName()));  // Tên khách hàng
//            row.addView(createCell(order.getPhoneNumber()));  // SĐT
//            row.addView(createCell(order.getDeliveryAddress()));  // Địa chỉ
//            row.addView(createCell(order.getFormattedDate()));  // Ngày nhận
//
//            // ✅ Xử lý trạng thái đơn hàng
//            String status = order.getCondition();
//            if ("Return_to_shop".equals(status)) {
//                status = "Trả về cửa hàng";
//            } else if ("Delivered_Successfully".equals(status)) {
//                status = "Giao hàng thành công";
//            }
//            row.addView(createCell(status));  // Trạng thái
//
//            tableOrders.addView(row);
//            index++;
//        }
//    }

    private TextView createHeaderCell(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(12, 8, 12, 8);
        textView.setTextSize(16);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(getResources().getColor(R.color.colorPinkSoft)); // Màu nền header
        textView.setGravity(Gravity.CENTER);
        return textView;
    }



    private void openOrderDetail(int orderID) {
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        intent.putExtra("ORDER_ID", orderID);
        startActivity(intent);
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
