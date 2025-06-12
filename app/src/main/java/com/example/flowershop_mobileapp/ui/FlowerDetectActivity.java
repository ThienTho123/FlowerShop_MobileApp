package com.example.flowershop_mobileapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;

import com.bumptech.glide.Glide;
import com.example.flowershop_mobileapp.R;
import com.example.flowershop_mobileapp.databinding.ActivityFlowerDetectBinding;
import com.example.flowershop_mobileapp.network.ApiClient;
import com.example.flowershop_mobileapp.network.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlowerDetectActivity extends AppCompatActivity {

    private ActivityFlowerDetectBinding binding;
    private ApiService apiService;
    private Uri selectedImageUri;
    private FlowerDetectResponse detectResponse;
    private FlowerDetectAdapter detectAdapter;
    private FlowerProductAdapter productAdapter;
    private int currentDetectIndex = 0;

    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlowerDetectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = ApiClient.getApiService(this);

        setupUI();
        setupClickListeners();
    }

    private void setupUI() {
        // Setup RecyclerView for detected objects
        binding.recyclerDetectedObjects.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        // Add snap helper for smooth scrolling
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerDetectedObjects);

        // Setup RecyclerView for recommended products
        binding.recyclerRecommendedProducts.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        // Hide result sections initially
        binding.layoutResults.setVisibility(View.GONE);
        binding.layoutRecommendedProducts.setVisibility(View.GONE);
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSelectImage.setOnClickListener(v -> showImageSourceDialog());

        binding.btnAnalyze.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                analyzeImage();
            } else {
                Toast.makeText(this, "Vui lòng chọn ảnh trước khi phân tích", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnPrevious.setOnClickListener(v -> {
            if (currentDetectIndex > 0) {
                currentDetectIndex--;
                updateDetectDisplay();
            }
        });

        binding.btnNext.setOnClickListener(v -> {
            if (detectResponse != null && currentDetectIndex < detectResponse.getObjects().size() - 1) {
                currentDetectIndex++;
                updateDetectDisplay();
            }
        });
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn nguồn ảnh")
                .setItems(new CharSequence[]{"Chụp ảnh", "Chọn từ thư viện"}, (dialog, which) -> {
                    if (which == 0) {
                        captureImage();
                    } else {
                        chooseImageFromGallery();
                    }
                })
                .show();
    }

    private void captureImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE && data != null && data.getData() != null) {
                // Image from gallery
                selectedImageUri = data.getData();
                displaySelectedImage();
            } else if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                // Image from camera
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    selectedImageUri = saveBitmapToFile(photo);
                    displaySelectedImage();
                }
            }
        }
    }

    private Uri saveBitmapToFile(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "flower_detect_" + timeStamp + ".jpg");

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FlowerDetect", "Error saving captured image");
            return null;
        }
    }

    private void displaySelectedImage() {
        if (selectedImageUri != null) {
            Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .into(binding.imgOriginal);

            binding.btnAnalyze.setEnabled(true);
            binding.layoutResults.setVisibility(View.GONE);
            binding.layoutRecommendedProducts.setVisibility(View.GONE);
        }
    }

    private void analyzeImage() {
        if (selectedImageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh trước", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoadingState(true);

        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), toByteArray(inputStream));
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", "flower_image.jpg", requestBody);

            apiService.detectFlower(body).enqueue(new Callback<FlowerDetectResponse>() {
                @Override
                public void onResponse(Call<FlowerDetectResponse> call, Response<FlowerDetectResponse> response) {
                    showLoadingState(false);

                    if (response.isSuccessful() && response.body() != null) {
                        detectResponse = response.body();
                        currentDetectIndex = 0;
                        displayDetectionResults();
                    } else {
                        Toast.makeText(FlowerDetectActivity.this, "Không thể phân tích ảnh. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        Log.e("FlowerDetect", "Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<FlowerDetectResponse> call, Throwable t) {
                    showLoadingState(false);
                    Toast.makeText(FlowerDetectActivity.this, "Lỗi kết nối. Vui lòng kiểm tra mạng!", Toast.LENGTH_SHORT).show();
                    Log.e("FlowerDetect", "API call failed", t);
                }
            });

        } catch (Exception e) {
            showLoadingState(false);
            Toast.makeText(this, "Không thể đọc ảnh!", Toast.LENGTH_SHORT).show();
            Log.e("FlowerDetect", "Error reading image", e);
        }
    }

    private void displayDetectionResults() {
        if (detectResponse == null || detectResponse.getObjects() == null || detectResponse.getObjects().isEmpty()) {
            Toast.makeText(this, "Không nhận diện được loài hoa nào trong ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        // Display result image
        if (detectResponse.getImage() != null) {
            String base64Image = "data:image/jpeg;base64," + detectResponse.getImage();
            Glide.with(this)
                    .load(base64Image)
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .into(binding.imgResult);
        }

        // Show results layout
        binding.layoutResults.setVisibility(View.VISIBLE);

        // Update detection display
        updateDetectDisplay();

        // Setup navigation buttons
        updateNavigationButtons();
    }

    private void updateDetectDisplay() {
        if (detectResponse == null || detectResponse.getObjects() == null ||
                currentDetectIndex >= detectResponse.getObjects().size()) {
            return;
        }

        FlowerDetectResponse.DetectedObject currentObject = detectResponse.getObjects().get(currentDetectIndex);
        FlowerDetectResponse.FlowerDetectInfo detectInfo = currentObject.getDetect();

        if (detectInfo != null) {
            // Update flower information
            binding.txtFlowerName.setText(detectInfo.getVietnamname() != null ? detectInfo.getVietnamname() : "Không xác định");
            binding.txtOrigin.setText("Xuất xứ: " + (detectInfo.getOrigin() != null ? detectInfo.getOrigin() : "Không có thông tin"));
            binding.txtTimeBloom.setText("Mùa nở: " + (detectInfo.getTimebloom() != null ? detectInfo.getTimebloom() : "Không có thông tin"));
            binding.txtNumberFound.setText("Số lượng: " + currentObject.getNumberFound() + " hoa");
            binding.txtCharacteristic.setText("Đặc điểm: " + (detectInfo.getCharacteristic() != null ? detectInfo.getCharacteristic() : "Không có thông tin"));
            binding.txtFlowerLanguage.setText("Ý nghĩa: " + (detectInfo.getFlowerlanguage() != null ? detectInfo.getFlowerlanguage() : "Không có thông tin"));
            binding.txtUses.setText("Công dụng: " + (detectInfo.getUses() != null ? detectInfo.getUses() : "Không có thông tin"));

            // Load flower image
            if (detectInfo.getImageurl() != null) {
                Glide.with(this)
                        .load(detectInfo.getImageurl())
                        .placeholder(R.drawable.ic_avatar_placeholder)
                        .into(binding.imgFlowerInfo);
            }
        }

        // Update pagination info
        binding.txtPagination.setText((currentDetectIndex + 1) + "/" + detectResponse.getObjects().size());

        // Update recommended products
        updateRecommendedProducts(currentObject.getFlowerDTOList());

        // Update navigation buttons
        updateNavigationButtons();
    }

    private void updateRecommendedProducts(List<FlowerDetectResponse.FlowerProduct> products) {
        if (products != null && !products.isEmpty()) {
            binding.layoutRecommendedProducts.setVisibility(View.VISIBLE);

            if (productAdapter == null) {
                productAdapter = new FlowerProductAdapter(products, this::onProductClick);
                binding.recyclerRecommendedProducts.setAdapter(productAdapter);
            } else {
                productAdapter.updateProducts(products);
            }
        } else {
            binding.layoutRecommendedProducts.setVisibility(View.GONE);
        }
    }

    private void onProductClick(FlowerDetectResponse.FlowerProduct product) {
        // Handle product click - navigate to product detail
        // Intent intent = new Intent(this, ProductDetailActivity.class);
        // intent.putExtra("product_id", product.getFlowerID());
        // startActivity(intent);

        Toast.makeText(this, "Xem chi tiết sản phẩm: " + product.getName(), Toast.LENGTH_SHORT).show();
    }

    private void updateNavigationButtons() {
        if (detectResponse == null || detectResponse.getObjects() == null) {
            binding.btnPrevious.setVisibility(View.GONE);
            binding.btnNext.setVisibility(View.GONE);
            return;
        }

        int totalObjects = detectResponse.getObjects().size();

        if (totalObjects <= 1) {
            binding.btnPrevious.setVisibility(View.GONE);
            binding.btnNext.setVisibility(View.GONE);
        } else {
            binding.btnPrevious.setVisibility(currentDetectIndex > 0 ? View.VISIBLE : View.INVISIBLE);
            binding.btnNext.setVisibility(currentDetectIndex < totalObjects - 1 ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void showLoadingState(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.txtLoadingMessage.setVisibility(View.VISIBLE);
            binding.btnAnalyze.setEnabled(false);
            binding.btnSelectImage.setEnabled(false);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.txtLoadingMessage.setVisibility(View.GONE);
            binding.btnAnalyze.setEnabled(true);
            binding.btnSelectImage.setEnabled(true);
        }
    }

    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding = null;
        }
    }
}