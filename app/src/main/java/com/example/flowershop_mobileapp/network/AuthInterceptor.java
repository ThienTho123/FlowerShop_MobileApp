package com.example.flowershop_mobileapp.network;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder();

        // Nếu có token, thêm vào Header
        if (!token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
        }

        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
