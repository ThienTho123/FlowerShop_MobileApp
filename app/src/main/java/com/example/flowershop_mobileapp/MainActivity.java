package com.example.flowershop_mobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các thành phần
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập Navigation Controller
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_account,
                R.id.nav_delivered_orders,
                R.id.nav_unassigned_orders,
                R.id.nav_pending_orders,
                R.id.nav_change_password,
                R.id.nav_logout
        ).setOpenableLayout(drawerLayout).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Xử lý sự kiện khi chọn item trong Navigation Drawer
        navView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_logout) {
                showLogoutDialog(); // Để hộp thoại đăng xuất hiển thị từ MainActivity
            } else {
                NavigationUI.onNavDestinationSelected(menuItem, navController);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        });

    }

    // Xử lý nút back để đóng drawer nếu đang mở
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    // Hiển thị hộp thoại xác nhận đăng xuất
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Xóa token lưu trữ (shared preferences hoặc database tùy theo cách bạn lưu)
                    clearUserToken();

                    // Tạo Intent để mở LoginActivity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                    // Xóa tất cả các activity trong back stack và bắt đầu một task mới
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);  // Mở LoginActivity
                    finish();  // Đóng MainActivity, không cho quay lại
                })
                .setNegativeButton("Không", null)
                .show();
    }

    // Hàm clear token
    private void clearUserToken() {
        // Ví dụ với SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("auth_token"); // Tên key của token
        editor.apply();
    }



}
