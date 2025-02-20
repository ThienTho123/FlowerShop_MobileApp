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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        // Kiểm tra các trường nhập liệu
        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmNewPassword)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(getContext(), "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng ChangePassword để gửi request
        ChangePassword changePasswordRequest = new ChangePassword(currentPassword, newPassword);

        apiService.changePassword(changePasswordRequest).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Đổi mật khẩu thành công
                    Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

                    // Đăng xuất: Xóa token
                    clearUserToken();

                    // Chuyển đến màn hình đăng nhập
                    redirectToLogin();
                } else {
                    // Nếu có lỗi, kiểm tra thông tin lỗi trả về từ server
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();  // Đọc nội dung lỗi trả về
                            Log.e("API_ERROR", errorBody); // Ghi lại nội dung lỗi để kiểm tra

                            // Phân tích lỗi JSON nếu có
                            JSONObject errorJson = new JSONObject(errorBody);
                            String errorMessage = errorJson.getString("message");  // Giả sử API trả về thông điệp lỗi trong "message"

                            // Kiểm tra nếu là lỗi mật khẩu hiện tại sai hoặc mật khẩu không khớp
                            if (errorMessage.contains("mật khẩu hiện tại không đúng")) {
                                Toast.makeText(getContext(), "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show();
                            } else if (errorMessage.contains("mật khẩu mới không khớp")) {
                                Toast.makeText(getContext(), "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Nếu không phải những lỗi trên, hiển thị thông báo thành công
                                Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

                                // Đăng xuất và chuyển đến trang đăng nhập
                                clearUserToken();
                                redirectToLogin();
                            }
                        } else {
                            Toast.makeText(getContext(), "Đã có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Trong trường hợp thất bại do kết nối, thông báo lỗi là không cần thiết nữa
                Toast.makeText(getContext(), "Đổi mật khẩu thất bại. Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    // Phương thức để xóa token và đăng xuất
    private void clearUserToken() {
        // Xóa token khỏi SharedPreferences hoặc nơi lưu trữ token của bạn
        SharedPreferences preferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("token");
        editor.apply();
    }

    // Phương thức chuyển đến màn hình đăng nhập
    private void redirectToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa hết các activity trước đó
        startActivity(intent);
        getActivity().finish(); // Đảm bảo đóng activity hiện tại
    }


}
