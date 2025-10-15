# 🎯 Hướng dẫn Setup Toàn bộ Hệ thống Giảm giá Nâng cao

## 📑 Mục lục
1. [Backend Setup](#backend-setup)
2. [Database Setup](#database-setup)
3. [Frontend Integration](#frontend-integration)
4. [Testing](#testing)
5. [Deployment](#deployment)

---

## 🔧 Backend Setup

### Bước 1: Cập nhật Dependencies

Mở file `pom.xml` và thêm các dependencies sau vào trong thẻ `<dependencies>`:

```xml
<!-- Hypersistence Utils for JSONB support -->
<dependency>
    <groupId>io.hypersistence</groupId>
    <artifactId>hypersistence-utils-hibernate-63</artifactId>
    <version>3.6.0</version>
</dependency>

<!-- Jackson for JSON -->
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>

<!-- Flyway for migrations -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

**Chạy lệnh:**
```bash
mvn clean install
```

### Bước 2: Cấu hình Database

Cập nhật `src/main/resources/application.properties`:

```properties
# Database Configuration (PostgreSQL)
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
```

### Bước 3: Verify Files đã tạo

Kiểm tra các files sau đã tồn tại:

**Models:**
- ✅ `Model/Promotion/DiscountPeriod.java`
- ✅ `Model/Promotion/ProductDiscountPeriod.java`

**DTOs:**
- ✅ `DTO/PromotionDTO/DiscountPeriodDTO.java`
- ✅ `DTO/PromotionDTO/AdvancedRulesDTO.java`
- ✅ `DTO/PromotionDTO/ProductDiscountPeriodDTO.java`

**Repositories:**
- ✅ `Repository/DiscountPeriodRepository.java`
- ✅ `Repository/ProductDiscountPeriodRepository.java`

**Services:**
- ✅ `Service/DiscountPeriodService.java`

**Controllers:**
- ✅ `Controller/DiscountPeriodController.java`

**Migrations:**
- ✅ `resources/db/migration/V1__create_discount_periods_tables.sql`
- ✅ `resources/db/migration/V2__insert_sample_discount_periods.sql`

---

## 🗄️ Database Setup

### Option 1: Automatic (Flyway - Recommended)

Flyway sẽ tự động chạy migration khi khởi động ứng dụng.

```bash
mvn spring-boot:run
```

### Option 2: Manual

```bash
# Kết nối PostgreSQL
psql -U postgres

# Tạo database
CREATE DATABASE your_database_name;

# Kết nối vào database
\c your_database_name

# Chạy migration scripts
\i src/main/resources/db/migration/V1__create_discount_periods_tables.sql
\i src/main/resources/db/migration/V2__insert_sample_discount_periods.sql

# Kiểm tra bảng đã tạo
\dt

# Xem dữ liệu mẫu
SELECT * FROM discount_periods;
```

### Verify Database

```sql
-- Kiểm tra cấu trúc bảng
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'discount_periods';

-- Kiểm tra dữ liệu mẫu
SELECT id, discount_period_code, discount_period_name, status 
FROM discount_periods;

-- Kiểm tra indexes
SELECT indexname, indexdef 
FROM pg_indexes 
WHERE tablename = 'discount_periods';
```

---

## 🚀 Khởi động Backend

```bash
cd main
mvn clean install
mvn spring-boot:run
```

**Kiểm tra backend đã chạy:**
```bash
curl http://localhost:8080/api/discount-periods
```

**Response mong đợi:**
```json
{
  "status": "success",
  "message": "Lấy danh sách discount periods thành công",
  "data": [...]
}
```

---

## 🎨 Frontend Integration

### Bước 1: Tạo Service File

Tạo file `src/service/discountPeriodService.jsx`:

```javascript
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const discountPeriodService = {
  // Discount Periods
  getAll: () => axios.get(`${API_URL}/discount-periods`),
  getById: (id) => axios.get(`${API_URL}/discount-periods/${id}`),
  getActive: () => axios.get(`${API_URL}/discount-periods/active`),
  create: (data) => axios.post(`${API_URL}/discount-periods`, data),
  update: (id, data) => axios.put(`${API_URL}/discount-periods/${id}`, data),
  delete: (id) => axios.delete(`${API_URL}/discount-periods/${id}`),
  
  // Advanced Rules
  getRules: (id) => axios.get(`${API_URL}/discount-periods/${id}/rules`),
  updateRules: (id, rules) => axios.put(`${API_URL}/discount-periods/${id}/rules`, rules),
  
  // Products
  getProducts: (id) => axios.get(`${API_URL}/discount-periods/${id}/products`),
  saveProducts: (id, products) => axios.post(`${API_URL}/discount-periods/${id}/products`, products),
};
```

### Bước 2: Update Dashboard Component

Trong `Dashboard.jsx`, load discount periods từ API:

```javascript
import { discountPeriodService } from '../service/discountPeriodService';

// Trong component
useEffect(() => {
  const fetchData = async () => {
    try {
      const [periodsRes, productsRes] = await Promise.all([
        discountPeriodService.getAll(),
        // ... other API calls
      ]);
      
      setDiscountPeriods(periodsRes.data.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };
  
  fetchData();
}, []);
```

### Bước 3: Update DiscountManagement Hook

Cập nhật `handleSaveAdvancedRules` trong `useDiscountManagement.jsx`:

```javascript
const handleSaveAdvancedRules = async (rules) => {
  if (!rulesEditingPeriod) return;
  
  try {
    const response = await discountPeriodService.updateRules(
      rulesEditingPeriod.id, 
      rules
    );

    if (response.data.status === 'success') {
      setShowAdvancedRulesModal(false);
      setRulesEditingPeriod(null);
      // Reload data
      window.location.reload();
    }
  } catch (error) {
    console.error('Error saving advanced rules:', error);
    alert('Lỗi khi lưu quy tắc: ' + error.message);
  }
};
```

---

## 🧪 Testing

### Test 1: Get All Discount Periods

```bash
curl http://localhost:8080/api/discount-periods
```

### Test 2: Create New Period

```bash
curl -X POST http://localhost:8080/api/discount-periods \
  -H "Content-Type: application/json" \
  -d '{
    "discountPeriodCode": "TEST2024",
    "discountPeriodName": "Test Period",
    "minPercentageValue": 5,
    "maxPercentageValue": 30,
    "startTime": "2024-01-01T00:00:00",
    "endTime": "2024-12-31T23:59:59",
    "status": 1
  }'
```

### Test 3: Update Advanced Rules

```bash
curl -X PUT http://localhost:8080/api/discount-periods/1/rules \
  -H "Content-Type: application/json" \
  -d '{
    "tieredRules": [{
      "minAmount": 1000000,
      "discountType": "fixed",
      "discountValue": 100000
    }],
    "freeShippingRule": {
      "minAmount": 500000,
      "maxShippingDiscount": 30000
    }
  }'
```

### Test 4: Frontend Integration

1. Khởi động frontend: `npm run dev`
2. Đăng nhập admin
3. Vào trang Khuyến mãi
4. Click "Đợt giảm giá"
5. Click nút "⚙️ Quy tắc nâng cao"
6. Thử cấu hình các rules
7. Click "Lưu tất cả quy tắc"
8. Kiểm tra console và database

---

## 📊 Workflow hoàn chỉnh

```
1. Admin tạo Discount Period
   ↓
2. Admin cấu hình Advanced Rules
   ↓
3. Admin chọn sản phẩm áp dụng
   ↓
4. Khách hàng thêm sản phẩm vào giỏ
   ↓
5. Frontend gọi API tính toán giảm giá
   ↓
6. Backend áp dụng tất cả rules
   ↓
7. Hiển thị kết quả chi tiết cho khách
   ↓
8. Khách thanh toán
```

---

## 🐛 Common Issues & Solutions

### Issue 1: "Table 'discount_periods' doesn't exist"
**Solution:** 
- Chạy migration scripts
- Hoặc set `spring.jpa.hibernate.ddl-auto=create` (chỉ development)

### Issue 2: "JSONB type not supported"
**Solution:**
- Đảm bảo đang dùng PostgreSQL
- Thêm dependency `hypersistence-utils`
- Nếu dùng SQL Server, đổi JSONB → NVARCHAR(MAX)

### Issue 3: CORS Error
**Solution:**
- Đã có `@CrossOrigin(origins = "*")` trong Controller
- Kiểm tra `CorsConfig.java`
- Thêm frontend URL vào allowed origins

### Issue 4: "Cannot deserialize JSON"
**Solution:**
- Kiểm tra format JSON trong request
- Đảm bảo field names match với DTO
- Check Jackson ObjectMapper configuration

---

## 📈 Performance Optimization

### Database Indexes
Đã tạo sẵn các indexes trong migration script:
- `discount_period_code`
- `status`
- `start_time, end_time`
- `product_id`
- `discount_period_id`

### Caching (Optional)

Thêm caching cho active periods:

```java
@Cacheable("activeDiscountPeriods")
public ResponseEntity<ResponseObject> getActiveDiscountPeriods() {
    // ...
}
```

Thêm dependency:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

---

## 🚢 Deployment

### Environment Variables

```bash
# Production
export DB_URL=jdbc:postgresql://prod-server:5432/prod_db
export DB_USER=prod_user
export DB_PASS=prod_password

# Application
export SERVER_PORT=8080
export CORS_ORIGINS=https://your-frontend-domain.com
```

### Docker (Optional)

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t discount-api .
docker run -p 8080:8080 -e DB_URL=$DB_URL discount-api
```

---

## ✅ Checklist triển khai

- [ ] Dependencies đã được thêm vào `pom.xml`
- [ ] Database configuration đúng trong `application.properties`
- [ ] Migration scripts đã chạy thành công
- [ ] Tất cả Entity, DTO, Repository, Service, Controller đã tạo
- [ ] Backend khởi động không lỗi
- [ ] API endpoints trả về response đúng
- [ ] Frontend service đã được tạo
- [ ] Frontend gọi API thành công
- [ ] CORS đã được cấu hình đúng
- [ ] Test các tính năng CRUD
- [ ] Test advanced rules
- [ ] Test apply products
- [ ] Kiểm tra performance với nhiều dữ liệu

---

## 📚 Resources

- [Complete API Documentation](DISCOUNT_API_SETUP.md)
- [Frontend Guide](../DA_2025/src/Util/DISCOUNT_SYSTEM_GUIDE.md)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [PostgreSQL JSONB](https://www.postgresql.org/docs/current/datatype-json.html)

---

## 🆘 Support

Nếu gặp vấn đề:
1. Check logs: `tail -f logs/spring-boot-app.log`
2. Check database: `SELECT * FROM discount_periods;`
3. Check frontend console
4. Tạo issue hoặc liên hệ team dev

---

**Happy Coding! 🚀**

**Version:** 1.0.0  
**Last Updated:** 14/10/2025

