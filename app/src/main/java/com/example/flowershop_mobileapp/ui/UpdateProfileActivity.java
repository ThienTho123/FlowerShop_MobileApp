package com.example.flowershop_mobileapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.flowershop_mobileapp.MainActivity;
import com.example.flowershop_mobileapp.R;
import com.example.flowershop_mobileapp.databinding.ActivityUpdateProfileBinding;
import com.example.flowershop_mobileapp.models.User;
import com.example.flowershop_mobileapp.network.ApiClient;
import com.example.flowershop_mobileapp.network.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    private ActivityUpdateProfileBinding binding;
    private ApiService apiService;
    private String token;
    private Uri avatarUri;

    private Uri cameraImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = ApiClient.getApiService(this);
        token = getSharedPreferences("auth", MODE_PRIVATE).getString("token", "");

        loadUserProfile();

        binding.btnSave.setOnClickListener(v -> updateUserProfile());
        binding.imgAvatar.setOnClickListener(v -> showImageSourceDialog());
        binding.btnBack.setOnClickListener(v -> navigateBackToMain());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateBackToMain();
    }

    private void navigateBackToMain() {
        Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn nguồn ảnh")
                .setItems(new CharSequence[]{"Chụp ảnh", "Chọn từ thư viện"}, (dialog, which) -> {
                    if (which == 0) {
                        captureImage(); // Chụp ảnh bằng camera
                    } else {
                        chooseImageFromGallery(); // Chọn ảnh từ thư viện
                    }
                })
                .show();
    }
    private void captureImage() {
        Log.d("DEBUG", "captureImage() được gọi!");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 300);  // 🚀 Gọi trực tiếp, không kiểm tra `resolveActivity()`
    }

    private File createImageFile() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "IMG_" + timeStamp;
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void loadUserProfile() {
        apiService.getUserProfile("Bearer " + token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    binding.etFullName.setText(user.getFullName());
                    binding.etEmail.setText(user.getEmail());
                    binding.etPhoneNumber.setText(user.getPhoneNumber());
                    binding.etAddress.setText(user.getAddress());

                    if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                        Glide.with(UpdateProfileActivity.this)
                                .load(user.getAvatarUrl())
                                .placeholder(R.drawable.ic_avatar_placeholder)
                                .into(binding.imgAvatar);
                    }
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "Không tải được thông tin!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile() {
        String fullName = binding.etFullName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String phoneNumber = binding.etPhoneNumber.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();

        User updatedUser = new User(fullName, phoneNumber, email, address, avatarUri != null ? avatarUri.toString() : null);

        apiService.updateUserProfile("Bearer " + token, updatedUser).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100 && data != null && data.getData() != null) {
                // Ảnh từ thư viện
                avatarUri = data.getData();
                uploadImageToServer(avatarUri);
            } else if (requestCode == 300 && data != null) {
                // Ảnh chụp từ Camera (Bitmap)
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    avatarUri = saveBitmapToFile(photo); // ✅ Lưu ảnh trước khi upload
                    uploadImageToServer(avatarUri);
                } else {
                    Log.e("DEBUG", "Không thể lấy ảnh chụp từ Camera!");
                }
            }
        }
    }
    private Uri saveBitmapToFile(Bitmap bitmap) {
        File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "profile_avatar.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DEBUG", "Lỗi khi lưu ảnh chụp!");
            return null;
        }
    }


    private void uploadImageToServer(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), toByteArray(inputStream));
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg", requestBody);

            apiService.uploadImage("Bearer " + token, body).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String imageUrl = (String) response.body().get("DT");
                        avatarUri = Uri.parse(imageUrl);
                        Glide.with(UpdateProfileActivity.this).load(imageUrl).into(binding.imgAvatar);
                    } else {
                        Toast.makeText(UpdateProfileActivity.this, "Upload ảnh thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Log.e("Upload", "Lỗi upload ảnh: " + t.getMessage());
                    Toast.makeText(UpdateProfileActivity.this, "Lỗi kết nối khi upload ảnh!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Không thể mở ảnh!", Toast.LENGTH_SHORT).show();
        }
    }


    // Chuyển InputStream thành byte array
    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }


    private String getRealPathFromURI(Context context, Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
