package com.example.flowershop_mobileapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.flowershop_mobileapp.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.orderIdTextView.setText("Đơn hàng ID: " + order.getId());

        holder.customerNameTextView.setText("Khách hàng: " + order.getCustomerName());

        holder.totalAmountTextView.setText("Tổng tiền: " + order.getQuantity());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView customerNameTextView;
        TextView totalAmountTextView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            customerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            totalAmountTextView = itemView.findViewById(R.id.totalAmountTextView);
        }
    }
}
