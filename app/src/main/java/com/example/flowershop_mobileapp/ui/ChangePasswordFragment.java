package com.example.flowershop_mobileapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.flowershop_mobileapp.LoginActivity;
import com.example.flowershop_mobileapp.R;
import com.example.flowershop_mobileapp.models.ChangePassword;
import com.example.flowershop_mobileapp.network.ApiClient;
import com.example.flowershop_mobileapp.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

    private EditText edtCurrentPassword, edtNewPassword, edtConfirmNewPassword;
    private Button btnChangePassword;
    private ApiService apiService;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        edtCurrentPassword = view.findViewById(R.id.edtCurrentPassword);
        edtNewPassword = view.findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = view.findViewById(R.id.edtConfirmNewPassword);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);

        apiService = ApiClient.getApiService(getContext());

        btnChangePassword.setOnClickListener(v -> changePassword());

        return view;
    }

    private void changePassword() {
        String currentPassword = edtCurrentPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmNewPassword = edtConfirmNewPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmNewPassword)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(getContext(), "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        ChangePassword changePasswordRequest = new ChangePassword(currentPassword, newPassword);

        apiService.changePassword(changePasswordRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("ChangePasswordAPI", "Đổi mật khẩu thành công!");
                    Toast.makeText(getContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();

                    clearUserToken();
                    redirectToLogin();
                } else {
                    Log.e("ChangePasswordAPI", "Lỗi từ server: " + response.code());
                    Toast.makeText(getContext(), "Đổi mật khẩu thất bại! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ChangePasswordAPI", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối! Vui lòng kiểm tra mạng.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearUserToken() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("token");
        editor.apply();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
