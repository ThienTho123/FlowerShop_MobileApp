package com.example.flowershop_mobileapp.ui;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flowershop_mobileapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FlowerProductAdapter extends RecyclerView.Adapter<FlowerProductAdapter.ProductViewHolder> {

    private List<FlowerDetectResponse.FlowerProduct> products;
    private OnProductClickListener listener;
    private NumberFormat currencyFormat;

    public interface OnProductClickListener {
        void onProductClick(FlowerDetectResponse.FlowerProduct product);
    }

    public FlowerProductAdapter(List<FlowerDetectResponse.FlowerProduct> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flower_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        FlowerDetectResponse.FlowerProduct product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public void updateProducts(List<FlowerDetectResponse.FlowerProduct> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView txtProductName, txtPrice, txtPriceEvent, txtCategory, txtPurpose;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            txtProductName = itemView.findViewById(R.id.txt_product_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtPriceEvent = itemView.findViewById(R.id.txt_price_event);
            txtCategory = itemView.findViewById(R.id.txt_category);
            txtPurpose = itemView.findViewById(R.id.txt_purpose);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onProductClick(products.get(getAdapterPosition()));
                }
            });
        }

        public void bind(FlowerDetectResponse.FlowerProduct product) {
            txtProductName.setText(product.getName());

            // Load product image
            Glide.with(itemView.getContext())
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .into(imgProduct);

            // Handle pricing
            if (product.getPriceEvent() != null && product.getPriceEvent() > 0) {
                // Has discount price
                txtPrice.setText(formatPrice(product.getPrice()));
                txtPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                txtPrice.setVisibility(View.VISIBLE);

                txtPriceEvent.setText(formatPrice(product.getPriceEvent()));
                txtPriceEvent.setVisibility(View.VISIBLE);
            } else {
                // Regular price only
                txtPrice.setText(formatPrice(product.getPrice()));
                txtPrice.setPaintFlags(txtPrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                txtPrice.setVisibility(View.VISIBLE);

                txtPriceEvent.setVisibility(View.GONE);
            }

            // Category and Purpose
            if (product.getCategory() != null) {
                txtCategory.setText("Danh mục: " + product.getCategory().getCategoryName());
                txtCategory.setVisibility(View.VISIBLE);
            } else {
                txtCategory.setVisibility(View.GONE);
            }

            if (product.getPurpose() != null) {
                txtPurpose.setText("Mục đích: " + product.getPurpose().getPurposeName());
                txtPurpose.setVisibility(View.VISIBLE);
            } else {
                txtPurpose.setVisibility(View.GONE);
            }
        }

        private String formatPrice(double price) {
            return String.format(Locale.getDefault(), "%,.0f đ", price);
        }
    }
}