# 🚀 Hướng dẫn Setup Backend API cho Hệ thống Giảm giá Nâng cao

## 📋 Tổng quan

Backend API hỗ trợ đầy đủ các tính năng giảm giá nâng cao:
- CRUD Discount Periods
- Quản lý Advanced Rules (Tiered, Buy X Get Y, Bundle, Gifts, Free Shipping)
- Áp dụng giảm giá cho sản phẩm
- Tính toán giảm giá khi checkout

## 🔧 Yêu cầu

- Java 17+
- PostgreSQL 12+ (để sử dụng JSONB)
- Maven 3.6+
- Spring Boot 3.x

## 📦 Dependencies cần thêm vào `pom.xml`

```xml
<dependencies>
    <!-- Hypersistence Utils for JSONB support -->
    <dependency>
        <groupId>io.hypersistence</groupId>
        <artifactId>hypersistence-utils-hibernate-63</artifactId>
        <version>3.6.0</version>
    </dependency>
    
    <!-- Jackson for JSON processing -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
    
    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

## 🗄️ Cấu hình Database

### 1. Tạo Database (nếu chưa có)

```sql
CREATE DATABASE your_database_name;
```

### 2. Cập nhật `application.properties`

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# JSON Type
spring.jpa.properties.hibernate.types.print.banner=false
```

### 3. Chạy Migration Scripts

Migration scripts đã được tạo sẵn trong:
- `src/main/resources/db/migration/V1__create_discount_periods_tables.sql`
- `src/main/resources/db/migration/V2__insert_sample_discount_periods.sql`

**Option 1: Chạy thủ công qua pgAdmin hoặc psql**

```bash
psql -U your_username -d your_database_name -f src/main/resources/db/migration/V1__create_discount_periods_tables.sql
psql -U your_username -d your_database_name -f src/main/resources/db/migration/V2__insert_sample_discount_periods.sql
```

**Option 2: Sử dụng Flyway (Recommended)**

Thêm dependency:
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

Cấu hình trong `application.properties`:
```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
```

## 🚀 Khởi động Backend

```bash
cd main
mvn clean install
mvn spring-boot:run
```

Backend sẽ chạy tại: `http://localhost:8080`

## 📡 API Endpoints

### 1. **Discount Periods Management**

#### Get All Discount Periods
```
GET /api/discount-periods
Response: { status: "success", message: "...", data: [...] }
```

#### Get Discount Period by ID
```
GET /api/discount-periods/{id}
Response: { status: "success", message: "...", data: {...} }
```

#### Get Active Discount Periods
```
GET /api/discount-periods/active
Response: { status: "success", message: "...", data: [...] }
```

#### Create Discount Period
```
POST /api/discount-periods
Content-Type: application/json

Body:
{
  "discountPeriodCode": "TET2024",
  "discountPeriodName": "Khuyến mãi Tết 2024",
  "minPercentageValue": 5,
  "maxPercentageValue": 50,
  "orderMinTotal": 500000,
  "orderMinItems": 2,
  "orderDiscountType": "percent",
  "orderDiscountValue": 10,
  "orderGiftDescription": "Tặng lịch tết",
  "enableOrderRule": true,
  "startTime": "2024-01-20T00:00:00",
  "endTime": "2024-02-15T23:59:59",
  "status": 1
}
```

#### Update Discount Period
```
PUT /api/discount-periods/{id}
Content-Type: application/json

Body: (same as create)
```

#### Delete Discount Period
```
DELETE /api/discount-periods/{id}
Response: { status: "success", message: "Xóa thành công" }
```

### 2. **Advanced Rules Management**

#### Update Advanced Rules
```
PUT /api/discount-periods/{id}/rules
Content-Type: application/json

Body:
{
  "tieredRules": [
    {
      "minAmount": 1000000,
      "discountType": "fixed",
      "discountValue": 100000,
      "maxDiscount": null
    },
    {
      "minAmount": 5000000,
      "discountType": "percent",
      "discountValue": 10,
      "maxDiscount": 500000
    }
  ],
  "buyXGetYRules": [
    {
      "buyProductIds": ["1", "2", "3"],
      "buyQuantity": 2,
      "getProductIds": ["10", "11"],
      "getQuantity": 1,
      "getDiscountPercent": 100,
      "maxApplications": 1
    }
  ],
  "bundleRules": [],
  "uniqueProductRule": {
    "minUniqueProducts": 3,
    "discountType": "percent",
    "discountValue": 15,
    "maxDiscount": 500000
  },
  "giftRules": [
    {
      "minAmount": 5000000,
      "minItems": 0,
      "gifts": [
        {
          "description": "Tai nghe Bluetooth",
          "estimatedValue": 500000
        }
      ]
    }
  ],
  "freeShippingRule": {
    "minAmount": 500000,
    "maxShippingDiscount": 30000
  }
}
```

#### Get Advanced Rules
```
GET /api/discount-periods/{id}/rules
Response: { status: "success", message: "...", data: {...} }
```

### 3. **Product Discount Management**

#### Get Applied Products
```
GET /api/discount-periods/{id}/products
Response: { status: "success", message: "...", data: [...] }
```

#### Save Applied Products
```
POST /api/discount-periods/{id}/products
Content-Type: application/json

Body:
[
  {
    "productId": 1,
    "discountPeriodId": 1,
    "percentageValue": 10,
    "included": true
  },
  {
    "productId": 2,
    "discountPeriodId": 1,
    "percentageValue": 15,
    "included": true
  }
]
```

## 🔄 Cập nhật Frontend Service

Cập nhật file `src/service/getAPI.jsx` trong frontend:

```javascript
// Discount Periods API
export const discountPeriodAPI = {
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

## 🧪 Testing API với Postman/cURL

### Example: Create Discount Period

```bash
curl -X POST http://localhost:8080/api/discount-periods \
  -H "Content-Type: application/json" \
  -d '{
    "discountPeriodCode": "TEST2024",
    "discountPeriodName": "Test Discount Period",
    "minPercentageValue": 5,
    "maxPercentageValue": 30,
    "startTime": "2024-01-01T00:00:00",
    "endTime": "2024-12-31T23:59:59",
    "status": 1
  }'
```

### Example: Update Advanced Rules

```bash
curl -X PUT http://localhost:8080/api/discount-periods/1/rules \
  -H "Content-Type: application/json" \
  -d '{
    "tieredRules": [
      {
        "minAmount": 1000000,
        "discountType": "fixed",
        "discountValue": 100000,
        "maxDiscount": null
      }
    ],
    "freeShippingRule": {
      "minAmount": 500000,
      "maxShippingDiscount": 30000
    }
  }'
```

## 🐛 Troubleshooting

### Lỗi: "column of type jsonb does not exist"
**Giải pháp:** Đảm bảo sử dụng PostgreSQL và thêm dependency `hypersistence-utils`

### Lỗi: "could not execute statement; SQL [n/a]; constraint [...]"
**Giải pháp:** Kiểm tra foreign key constraints, đảm bảo product_id tồn tại trong bảng products

### Lỗi: CORS
**Giải pháp:** Đã có `@CrossOrigin(origins = "*")` trong Controller. Nếu vẫn lỗi, check CorsConfig.java

## 📊 Database Schema

```
discount_periods
├── id (BIGSERIAL PRIMARY KEY)
├── discount_period_code (VARCHAR UNIQUE)
├── discount_period_name (VARCHAR)
├── min_percentage_value (INTEGER)
├── max_percentage_value (INTEGER)
├── order_* (các trường cho order rules)
├── tiered_rules (JSONB)
├── buy_x_get_y_rules (JSONB)
├── bundle_rules (JSONB)
├── unique_product_rule (JSONB)
├── gift_rules (JSONB)
├── free_shipping_rule (JSONB)
├── start_time (TIMESTAMP)
├── end_time (TIMESTAMP)
└── status (INTEGER)

product_discount_periods
├── id (BIGSERIAL PRIMARY KEY)
├── product_id (BIGINT FK)
├── discount_period_id (BIGINT FK)
├── percentage_value (INTEGER)
└── included (BOOLEAN)
```

## 📚 Tài liệu thêm

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [PostgreSQL JSONB](https://www.postgresql.org/docs/current/datatype-json.html)
- [Hypersistence Utils](https://github.com/vladmihalcea/hypersistence-utils)

## 🆘 Support

Nếu gặp vấn đề, tạo issue hoặc liên hệ team dev.

---

**Version:** 1.0.0  
**Last Updated:** 14/10/2025  
**Author:** Backend Development Team

