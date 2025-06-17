# 📱 Ứng Dụng Android - Shipper FlowerShop 🚚

Chào mừng bạn đến với **Ứng Dụng Android Shipper** - ứng dụng di động chuyên dụng dành cho đội ngũ giao hàng của hệ thống FlowerShop. Ứng dụng giúp shipper dễ dàng quản lý và theo dõi các đơn hàng giao hoa một cách hiệu quả.

## 🎯 Tổng Quan

**Ứng Dụng Android Shipper** cung cấp:
- 📦 **Quản lý đơn hàng**: Xem danh sách đơn hàng cần giao
- ✅ **Cập nhật trạng thái**: Xác nhận nhận hàng và hoàn thành giao hàng
- 📞 **Quản lí thông tin tài khoản**: Khả năng quản lí các thông tin liên quan đến tài khoản shipper

---

## 1. Yêu Cầu Hệ Thống

- **Android Studio** (phiên bản Arctic Fox trở lên)
- **Java JDK** (phiên bản 8 trở lên)
- **Android SDK** (API level 21 trở lên)
- **Thiết bị Android** hoặc **Emulator** để test
- **Backend FlowerShop** đã được cài đặt và chạy
- **IntelliJ IDEA** để chạy backend

---

## 2. Hướng Dẫn Cài Đặt

### Bước 1: Clone hoặc Tải Dự Án
Clone hoặc tải thư mục dự án từ link Github:
```bash
git clone https://github.com/ThienTho123/FlowerShop_MobileApp.git
```

### Bước 2: Lấy Địa Chỉ IP Máy Tính
1. Mở **Command Prompt (CMD)**
2. Nhập lệnh để lấy địa chỉ IP:
   ```cmd
   ipconfig
   ```
3. Ghi lại **IPv4 Address** (ví dụ: `192.168.1.100`)

### Bước 3: Cấu Hình Backend IP
1. Truy cập vào **IntelliJ IDEA** và mở thư mục backend
2. Chỉnh sửa cấu hình IP của máy tính chạy backend:
   - Mở file `config/SecurityConfiguration`
   - Thay đổi địa chỉ IP thành IP máy tính chạy Backend

### Bước 4: Cấu Hình Android App
1. Truy cập vào **Android Studio** và mở thư mục app Android
2. Chỉnh sửa cấu hình để kết nối đến backend:

   **4.1. Cấu hình API Client:**
   - Mở file `network/ApiClient`
   - Thay đổi địa chỉ IP thành IP máy tính chạy Backend

   **4.2. Cấu hình Network Security:**
   - Mở file `xml/network_security_config.xml`
   - Thay đổi địa chỉ IP thành IP máy tính chạy Backend

### Bước 5: Chạy Backend Server
1. Chạy chương trình Backend đã thiết lập IP:
   - Bấm nút **"Run"** 
   - Hoặc chọn trực tiếp file `"SpringbootdemoApplication"`
   - Chọn **"Current File"** và bấm vào nút **"Run"** trên header

2. Xác nhận chạy thành công và xem hệ thống RestAPI:
   ```
   http://[IP_ADDRESS]:8080/swagger-ui/index.html
   ```

### Bước 6: Chạy Android App
1. Chạy chương trình App Android đã thiết lập IP:
   - Bấm nút **"Run"** trong Android Studio
   - Chọn thiết bị Android hoặc emulator để cài đặt

---

## 3. Tính Năng Chính

### 📋 Quản Lý Đơn Hàng
- Xem danh sách đơn hàng được phân công
- Chi tiết thông tin đơn hàng (sản phẩm, địa chỉ, khách hàng)
- Lịch sử giao hàng

### 🚚 Giao Hàng
- Cập nhật trạng thái "Đang giao hàng"
- Xác nhận "Đã giao thành công"
- Ghi chú lý do nếu giao hàng thất bại

### 📱 Giao Diện Người Dùng
- Giao diện thân thiện, dễ sử dụng
- Tối ưu cho màn hình di động

---

## 4. Cấu Hình Chi Tiết

### Cấu Hình IP Backend
Trong file `config/SecurityConfiguration`:
```java
// Thay đổi IP address tương ứng
@CrossOrigin(origins = "http://[YOUR_IP_ADDRESS]:8080")
```

### Cấu Hình IP Android App
Trong file `network/ApiClient`:
```java
public class ApiClient {
    private static final String BASE_URL = "http://[YOUR_IP_ADDRESS]:8080/api/";
}
```

Trong file `xml/network_security_config.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">[YOUR_IP_ADDRESS]</domain>
    </domain-config>
</network-security-config>
```

---

## 5. Kiểm Tra Kết Nối

### Backend API
Truy cập để kiểm tra backend đã chạy:
```
http://[YOUR_IP_ADDRESS]:8080/swagger-ui/index.html
```

### Android App
- Đảm bảo thiết bị Android và máy tính backend cùng mạng
- Kiểm tra firewall không chặn cổng 8080
- Test kết nối API từ app

---

## 6. Xử Lý Sự Cố

### Lỗi Kết Nối
- Kiểm tra IP address đã đúng
- Đảm bảo backend đang chạy
- Kiểm tra firewall và antivirus

### Lỗi Build Android
- Clean và rebuild project
- Sync project with Gradle files
- Kiểm tra phiên bản Android SDK

---

## 7. Ghi Chú

- Đảm bảo rằng cổng `8080` không bị xung đột
- Thiết bị Android và máy tính backend phải cùng mạng
- Nếu gặp lỗi, kiểm tra lại cấu hình IP hoặc liên hệ nhóm phát triển

---

## 8. Thông Tin Liên Hệ

- **Nhóm Phát Triển**:
  - Mobile App: [ThienTho123](https://github.com/ThienTho123)
  - Backend: [TXTThien](https://github.com/TXTThien)

- **Repository**: [FlowerShop Mobile App](https://github.com/ThienTho123/FlowerShop_MobileApp.git)
