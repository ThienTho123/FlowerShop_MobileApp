package com.example.flowershop_mobileapp.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flowershop_mobileapp.R;
import com.example.flowershop_mobileapp.models.Order;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.OrderViewHolder> {
    private List<Order> orderList = new ArrayList<>();

    public void setOrders(List<Order> orders) {
        this.orderList = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderID.setText("Mã đơn: " + order.getOrderID());
        holder.tvCustomer.setText("Khách hàng: " + order.getName() + " - " + order.getPhoneNumber());
        holder.tvAddress.setText("Địa chỉ: " + order.getDeliveryAddress());

        // ✅ Xử lý trạng thái đơn hàng
        String statusText;
        int textColor;

        switch (order.getCondition()) {
            case "Cancel_is_Processing":
                statusText = "Hủy đang xử lý";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorOrange);
                break;
            case "Cancelled":
                statusText = "Đã hủy";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorRed);
                break;
            case "In_Transit":
                statusText = "Đang vận chuyển";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorBlue);
                break;
            case "Shipper_Delivering":
                statusText = "Shipper đang giao hàng";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorBlue);
                break;
            case "First_Attempt_Failed":
                statusText = "Lần giao hàng đầu tiên thất bại";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorRed);
                break;
            case "Second_Attempt_Failed":
                statusText = "Lần giao hàng thứ hai thất bại";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorRed);
                break;
            case "Third_Attempt_Failed":
                statusText = "Lần giao hàng thứ ba thất bại";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorRed);
                break;
            case "Delivered_Successfully":
                statusText = "Giao hàng thành công";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorGreen);
                break;
            case "Return_to_shop":
                statusText = "Trả về cửa hàng";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorOrange);
                break;
            case "Pending":
                statusText = "Đang chờ xử lý";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorOrange);
                break;
            case "Processing":
                statusText = "Đang xử lý";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorBlue);
                break;
            case "Prepare":
                statusText = "Chuẩn bị";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorBlue);
                break;
            default:
                statusText = "Không xác định";
                textColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.black);
                break;
        }

        holder.tvStatus.setText("Trạng thái: " + statusText);
        holder.tvStatus.setTextColor(textColor);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PendingOrdersDetailActivity.class);
            intent.putExtra("ORDER_ID", order.getOrderID());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderID, tvCustomer, tvAddress, tvStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderID = itemView.findViewById(R.id.tvOrderID);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
