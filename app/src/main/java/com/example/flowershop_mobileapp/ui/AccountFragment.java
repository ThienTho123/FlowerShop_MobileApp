package com.example.flowershop_mobileapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.flowershop_mobileapp.R;
import com.example.flowershop_mobileapp.databinding.FragmentAccountBinding;
import com.example.flowershop_mobileapp.models.User;
import com.example.flowershop_mobileapp.network.ApiClient;
import com.example.flowershop_mobileapp.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private SharedPreferences sharedPreferences;
    private ApiService apiService;
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        apiService = ApiClient.getApiService(requireContext());

        sharedPreferences = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(getActivity(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return binding.getRoot();
        }

        setupToolbar();
        loadUserProfile(token);

        // Xử lý sự kiện cập nhật thông tin
        binding.btnUpdateProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> {
            DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void loadUserProfile(String token) {
        apiService.getUserProfile("Bearer " + token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    binding.tvShipperName.setText(user.getFullName());
                    binding.tvUsername.setText("Username: " + user.getUsername());
                    binding.tvPhoneNumber.setText("Số điện thoại: " + user.getPhoneNumber());
                    binding.tvEmail.setText("Email: " + user.getEmail());

                    // Hiển thị địa chỉ nếu có
                    if (user.getAddress() != null && !user.getAddress().isEmpty()) {
                        binding.tvAddress.setText("Địa chỉ: " + user.getAddress());
                    } else {
                        binding.tvAddress.setText("Địa chỉ: Chưa có thông tin");
                    }

                    if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                        Glide.with(requireContext())
                                .load(user.getAvatarUrl())
                                .placeholder(R.drawable.ic_avatar_placeholder)
                                .into(binding.imgAvatar);
                    }
                } else {
                    Toast.makeText(getActivity(), "Lỗi tải thông tin!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
