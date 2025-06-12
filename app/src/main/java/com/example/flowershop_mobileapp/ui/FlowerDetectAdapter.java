package com.example.flowershop_mobileapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flowershop_mobileapp.R;

import java.util.List;

public class FlowerDetectAdapter extends RecyclerView.Adapter<FlowerDetectAdapter.DetectViewHolder> {

    private List<FlowerDetectResponse.DetectedObject> detectedObjects;
    private OnDetectItemClickListener listener;

    public interface OnDetectItemClickListener {
        void onDetectItemClick(FlowerDetectResponse.DetectedObject detectedObject, int position);
    }

    public FlowerDetectAdapter(List<FlowerDetectResponse.DetectedObject> detectedObjects, OnDetectItemClickListener listener) {
        this.detectedObjects = detectedObjects;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DetectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flower_detect, parent, false);
        return new DetectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetectViewHolder holder, int position) {
        FlowerDetectResponse.DetectedObject detectedObject = detectedObjects.get(position);
        holder.bind(detectedObject, position);
    }

    @Override
    public int getItemCount() {
        return detectedObjects != null ? detectedObjects.size() : 0;
    }

    public void updateDetectedObjects(List<FlowerDetectResponse.DetectedObject> newDetectedObjects) {
        this.detectedObjects = newDetectedObjects;
        notifyDataSetChanged();
    }

    class DetectViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgFlower;
        private TextView txtFlowerName, txtOrigin, txtNumberFound, txtTimeBloom;
        private TextView txtCharacteristic, txtFlowerLanguage, txtUses;
        private View layoutExpandedInfo;
        private TextView btnToggleInfo;

        public DetectViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFlower = itemView.findViewById(R.id.img_flower);
            txtFlowerName = itemView.findViewById(R.id.txt_flower_name);
            txtOrigin = itemView.findViewById(R.id.txt_origin);
            txtNumberFound = itemView.findViewById(R.id.txt_number_found);
            txtTimeBloom = itemView.findViewById(R.id.txt_time_bloom);
            txtCharacteristic = itemView.findViewById(R.id.txt_characteristic);
            txtFlowerLanguage = itemView.findViewById(R.id.txt_flower_language);
            txtUses = itemView.findViewById(R.id.txt_uses);
            layoutExpandedInfo = itemView.findViewById(R.id.layout_expanded_info);
            btnToggleInfo = itemView.findViewById(R.id.btn_toggle_info);

            // Initially hide expanded info
            layoutExpandedInfo.setVisibility(View.GONE);
            btnToggleInfo.setText("Xem thêm");

            // Toggle expanded info
            btnToggleInfo.setOnClickListener(v -> {
                if (layoutExpandedInfo.getVisibility() == View.GONE) {
                    layoutExpandedInfo.setVisibility(View.VISIBLE);
                    btnToggleInfo.setText("Thu gọn");
                } else {
                    layoutExpandedInfo.setVisibility(View.GONE);
                    btnToggleInfo.setText("Xem thêm");
                }
            });

            // Item click listener
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onDetectItemClick(detectedObjects.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        public void bind(FlowerDetectResponse.DetectedObject detectedObject, int position) {
            FlowerDetectResponse.FlowerDetectInfo detectInfo = detectedObject.getDetect();

            if (detectInfo != null) {
                // Basic flower information
                txtFlowerName.setText(detectInfo.getVietnamname() != null ?
                        detectInfo.getVietnamname() : "Không xác định");

                txtOrigin.setText("Xuất xứ: " +
                        (detectInfo.getOrigin() != null ? detectInfo.getOrigin() : "Không có thông tin"));

                txtTimeBloom.setText("Mùa nở: " +
                        (detectInfo.getTimebloom() != null ? detectInfo.getTimebloom() : "Không có thông tin"));

                txtNumberFound.setText("Số lượng: " + detectedObject.getNumberFound() + " hoa");

                // Expanded information
                txtCharacteristic.setText("Đặc điểm: " +
                        (detectInfo.getCharacteristic() != null ? detectInfo.getCharacteristic() : "Không có thông tin"));

                txtFlowerLanguage.setText("Ý nghĩa: " +
                        (detectInfo.getFlowerlanguage() != null ? detectInfo.getFlowerlanguage() : "Không có thông tin"));

                txtUses.setText("Công dụng: " +
                        (detectInfo.getUses() != null ? detectInfo.getUses() : "Không có thông tin"));

                // Load flower image
                if (detectInfo.getImageurl() != null && !detectInfo.getImageurl().isEmpty()) {
                    Glide.with(itemView.getContext())
                            .load(detectInfo.getImageurl())
                            .placeholder(R.drawable.ic_avatar_placeholder)
                            .error(R.drawable.ic_avatar_placeholder)
                            .into(imgFlower);
                } else {
                    imgFlower.setImageResource(R.drawable.ic_avatar_placeholder);
                }
            } else {
                // Handle case when detectInfo is null
                txtFlowerName.setText("Không xác định");
                txtOrigin.setText("Xuất xứ: Không có thông tin");
                txtTimeBloom.setText("Mùa nở: Không có thông tin");
                txtNumberFound.setText("Số lượng: " + detectedObject.getNumberFound() + " hoa");
                txtCharacteristic.setText("Đặc điểm: Không có thông tin");
                txtFlowerLanguage.setText("Ý nghĩa: Không có thông tin");
                txtUses.setText("Công dụng: Không có thông tin");
                imgFlower.setImageResource(R.drawable.ic_avatar_placeholder);
            }

            // Reset expanded state for recycled views
            layoutExpandedInfo.setVisibility(View.GONE);
            btnToggleInfo.setText("Xem thêm");
        }
    }
}