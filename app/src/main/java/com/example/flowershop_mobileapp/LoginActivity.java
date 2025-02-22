package com.example.flowershop_mobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flowershop_mobileapp.models.AuthResponse;
import com.example.flowershop_mobileapp.models.LoginRequest;
import com.example.flowershop_mobileapp.models.User;
import com.example.flowershop_mobileapp.network.ApiClient;
import com.example.flowershop_mobileapp.network.ApiService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        apiService = ApiClient.getApiService(this);

        if (isLoggedIn()) { // Nếu đã đăng nhập, chuyển sang MainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(v -> loginUser());
    }

    // Kiểm tra xem token có hợp lệ không
    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        return token != null && !token.isEmpty();
    }

    private void loginUser() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(username, password);
        apiService = ApiClient.getApiService(this);

        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d("LoginAPI", "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    String token = authResponse.getAccessToken();

                    if (token != null && !token.isEmpty()) {
                        saveToken(token);

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Không nhận được token!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("LoginAPI", "Response error: " + response.errorBody());
                    Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("LoginAPI", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }
}
