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
    private TableLayout tableOrders;
    private SwipeRefreshLayout swipeRefresh;
    private static final int ROW_HEIGHT = 250; // Đặt chiều cao cố định cho tất cả hàng

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivered_orders, container, false);

        tableOrders = view.findViewById(R.id.tableOrders);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        // Xử lý sự kiện khi bấm nút quay lại
        toolbar.setNavigationOnClickListener(v -> navigateBackToMain());

        fetchOrders();
        swipeRefresh.setOnRefreshListener(this::fetchOrders);

        return view;
    }

    private void fetchOrders() {
        swipeRefresh.setRefreshing(true);

        ApiClient.getApiService(getContext()).getDeliveredOrders().enqueue(new Callback<Map<String, List<Order>>>() {
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

        int index = 1;
        for (Order order : orders) {
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    ROW_HEIGHT)); // Thiết lập chiều cao cố định cho hàng

            row.addView(createCell(String.valueOf(index), (int) 1.6));
            row.addView(createCell(String.valueOf(order.getOrderID()), (int) 1.2));
            row.addView(createCell(order.getName(), (int) 2));
            row.addView(createCell(order.getPhoneNumber(), (int) 2));
            row.addView(createCell(order.getDeliveryAddress(), (int) 3.5));
            row.addView(createCell(order.getFormattedDate(), (int) 2.1));

            // Thay đổi trạng thái trước khi hiển thị
            String status = order.getCondition();
            if ("Return_to_shop".equals(status)) {
                status = "Trả về cửa hàng";
            } else if ("Delivered_Successfully".equals(status)) {
                status = "Giao hàng thành công";
            }

            row.addView(createCell(status, 2));

            tableOrders.addView(row);
            index++;
        }
    }


    private TextView createCell(String text, int weight) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.drawable.cell_border);
        textView.setTextSize(16); // Cỡ chữ dễ đọc hơn
        textView.setMinHeight(ROW_HEIGHT); // Đảm bảo chiều cao tối thiểu
        textView.setHeight(ROW_HEIGHT); // Chiều cao cố định

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, ROW_HEIGHT, weight);
        textView.setLayoutParams(params);

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
