# 🎯 Hệ thống Giảm giá Nâng cao - MSSQL Backend Guide

## 📋 Tổng quan

Hệ thống discount đã được tích hợp hoàn toàn vào cấu trúc backend hiện tại với:
- ✅ **Database: MSSQL** (SQL Server 2016+)
- ✅ **Framework: Spring Boot + JPA**
- ✅ **3 bảng chính đã có: `discount_campaigns`, `discount_targets`, `vouchers`**

## 🗄️ Kiến trúc Database

### 1. **discount_campaigns** - Chiến dịch khuyến mãi
Bảng chính chứa thông tin chiến dịch và **các quy tắc nâng cao** (JSON).

```sql
discount_campaigns
├── id (INT, PK)
├── name (NVARCHAR) - Tên chiến dịch: "Khuyến mãi Tết 2024"
├── description (NVARCHAR(MAX))
├── campaign_type (NVARCHAR) - 'SALE', 'TET', 'SUMMER'...
├── start_date, end_date
├── is_active (BIT)
├── banner_image_url
├── priority (INT) - Độ ưu tiên
│
├── ============ QUY TẮC NÂNG CAO (JSON) ============
├── tiered_rules (NVARCHAR(MAX)) - Giảm theo bậc thang
├── buy_x_get_y_rules (NVARCHAR(MAX)) - Mua X tặng Y
├── bundle_rules (NVARCHAR(MAX)) - Combo
├── unique_product_rule (NVARCHAR(MAX)) - Mua nhiều SP khác nhau
├── gift_rules (NVARCHAR(MAX)) - Quà tặng
├── free_shipping_rule (NVARCHAR(MAX)) - Free ship
│
├── ============ QUY TẮC HÓA ĐƠN ============
├── min_order_amount (INT) - Đơn tối thiểu
├── min_items_count (INT) - Số SP tối thiểu
├── order_discount_type (NVARCHAR) - 'percent'/'fixed'
├── order_discount_value (INT)
├── order_gift_description (NVARCHAR)
│
└── created_at, updated_at
```

### 2. **discount_targets** - Áp dụng cho sản phẩm cụ thể
Ánh xạ chiến dịch với Product/Variant/Category.

```sql
discount_targets
├── id (INT, PK)
├── campaign_id (INT, FK) - ID chiến dịch
├── target_type (NVARCHAR) - 'PRODUCT', 'VARIANT', 'CATEGORY'
├── target_id (INT) - ID của product/variant/category
├── percentage_value (INT) - % giảm giá
├── is_included (BIT) - Có áp dụng không
├── fixed_discount_value (INT) - Giảm cố định (nếu không dùng %)
├── min_quantity (INT) - Số lượng tối thiểu
├── max_quantity (INT) - Số lượng tối đa
└── created_at, updated_at
```

**Ví dụ:**
- **PRODUCT**: `{campaign_id: 1, target_type: 'PRODUCT', target_id: 5}` → Giảm cho toàn bộ iPhone 15
- **VARIANT**: `{campaign_id: 1, target_type: 'VARIANT', target_id: 25}` → Chỉ giảm cho variant "iPhone 15 Pro Max 256GB Đen"
- **CATEGORY**: `{campaign_id: 1, target_type: 'CATEGORY', target_id: 2}` → Giảm cho tất cả điện thoại

### 3. **vouchers** - Mã giảm giá
Voucher code khách hàng nhập khi checkout.

```sql
vouchers
├── id (INT, PK)
├── code (NVARCHAR, UNIQUE) - Mã: "SUMMER2024", "TET15"
├── name, description
├── discount_type (NVARCHAR) - 'percentage' / 'fixed_amount'
├── value (DECIMAL) - Giá trị giảm
├── max_discount (DECIMAL) - Giảm tối đa
├── min_order_value (DECIMAL) - Đơn tối thiểu
├── total_uses, max_uses, max_uses_per_user
├── start_date, end_date
├── is_active (BIT)
├── campaign_id (INT, FK) - Liên kết với campaign
├── applicable_to (NVARCHAR) - 'ALL', 'CATEGORY', 'PRODUCT'
├── applicable_ids (NVARCHAR(MAX)) - JSON: ["1", "2", "3"]
├── user_type (NVARCHAR) - 'ALL', 'NEW', 'VIP'
├── priority (INT)
└── created_at, updated_at
```

## 🚀 Setup Instructions

### Bước 1: Chạy Migration Script

```sql
-- Mở SQL Server Management Studio
-- Kết nối database: trungghieu
-- Chạy file: 01_update_discount_tables.sql
```

Script sẽ:
- ✅ Thêm các columns mới vào bảng đã tồn tại
- ✅ Tạo indexes để tối ưu query
- ✅ Thêm comments mô tả

### Bước 2: Verify Database

```sql
-- Kiểm tra các columns đã được thêm
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'discount_campaigns';

-- Kiểm tra indexes
SELECT name, type_desc 
FROM sys.indexes 
WHERE object_id = OBJECT_ID('discount_campaigns');
```

### Bước 3: Backend đã Update

Models đã được cập nhật:
- ✅ `DiscountCampaign.java` - Thêm advanced rules fields
- ✅ `DiscountTarget.java` - Mở rộng với percentage_value, is_included...
- ✅ `Voucher.java` - Thêm applicable_to, user_type...

### Bước 4: Khởi động Backend

```bash
cd main
mvn clean install
mvn spring-boot:run
```

## 📊 Các loại Quy tắc Giảm giá

### 1. Tiered Discounts (Giảm theo bậc thang)

**Campaign config:**
```json
{
  "tieredRules": [
    {"minAmount": 1000000, "discountType": "fixed", "discountValue": 100000},
    {"minAmount": 3000000, "discountType": "fixed", "discountValue": 400000},
    {"minAmount": 5000000, "discountType": "percent", "discountValue": 10, "maxDiscount": 800000}
  ]
}
```

**Ví dụ:**
- Đơn 1.5tr → Giảm 100k
- Đơn 4tr → Giảm 400k
- Đơn 10tr → Giảm 10% (tối đa 800k) = 800k

### 2. Buy X Get Y (Mua X tặng Y)

**Campaign config:**
```json
{
  "buyXGetYRules": [
    {
      "buyProductIds": ["1", "2", "3"],
      "buyQuantity": 2,
      "getProductIds": ["10", "11"],
      "getQuantity": 1,
      "getDiscountPercent": 100,
      "maxApplications": 1
    }
  ]
}
```

**Ví dụ:** Mua 2 iPhone bất kỳ → Tặng 1 ốp lưng (giảm 100%)

### 3. Unique Products Discount (Mua nhiều SP khác nhau)

**Campaign config:**
```json
{
  "uniqueProductRule": {
    "minUniqueProducts": 3,
    "discountType": "percent",
    "discountValue": 15,
    "maxDiscount": 500000
  }
}
```

**Ví dụ:** Mua 3 sản phẩm khác nhau → Giảm 15% (tối đa 500k)

### 4. Gift Rewards (Quà tặng)

**Campaign config:**
```json
{
  "giftRules": [
    {
      "minAmount": 5000000,
      "minItems": 0,
      "gifts": [
        {"description": "Tai nghe Bluetooth AirPods", "estimatedValue": 1500000}
      ]
    },
    {
      "minAmount": 10000000,
      "minItems": 0,
      "gifts": [
        {"description": "Apple Watch SE", "estimatedValue": 8000000}
      ]
    }
  ]
}
```

### 5. Free Shipping

**Campaign config:**
```json
{
  "freeShippingRule": {
    "minAmount": 500000,
    "maxShippingDiscount": 30000
  }
}
```

### 6. Bundle/Combo Discounts

**Campaign config:**
```json
{
  "bundleRules": [
    {
      "requiredProducts": [
        {"productId": "1", "minQuantity": 1},
        {"productId": "5", "minQuantity": 1}
      ],
      "discountType": "percent",
      "discountValue": 20,
      "maxDiscount": 1000000
    }
  ]
}
```

**Ví dụ:** Mua iPhone + AirPods → Giảm 20%

## 💻 Ví dụ sử dụng

### Case 1: Tạo chiến dịch "Tết 2024"

```sql
INSERT INTO discount_campaigns (
    name, 
    description,
    campaign_type,
    start_date,
    end_date,
    tiered_rules,
    free_shipping_rule,
    min_order_amount,
    min_items_count,
    order_discount_type,
    order_discount_value,
    order_gift_description,
    is_active,
    priority
) VALUES (
    N'Khuyến mãi Tết Nguyên Đán 2024',
    N'Giảm giá khủng mừng Tết Nguyên Đán',
    'TET',
    '2024-01-20 00:00:00',
    '2024-02-15 23:59:59',
    N'[
        {"minAmount": 1000000, "discountType": "fixed", "discountValue": 100000},
        {"minAmount": 3000000, "discountType": "fixed", "discountValue": 400000},
        {"minAmount": 5000000, "discountType": "fixed", "discountValue": 800000}
    ]',
    N'{"minAmount": 500000, "maxShippingDiscount": 30000}',
    500000,
    2,
    'fixed',
    50000,
    N'Tặng lịch tết khi mua từ 2 sản phẩm',
    1,
    10
);
```

### Case 2: Áp dụng giảm giá cho sản phẩm

```sql
-- Giảm 10% cho toàn bộ iPhone 15 Pro Max (product_id = 5)
INSERT INTO discount_targets (
    campaign_id, 
    target_type, 
    target_id, 
    percentage_value, 
    is_included
) VALUES (1, 'PRODUCT', 5, 10, 1);

-- Giảm 15% cho variant "iPhone 15 Pro Max 256GB Đen" (variant_id = 25)
INSERT INTO discount_targets (
    campaign_id, 
    target_type, 
    target_id, 
    percentage_value, 
    is_included
) VALUES (1, 'VARIANT', 25, 15, 1);

-- Giảm 5% cho toàn danh mục "Điện thoại" (category_id = 2)
INSERT INTO discount_targets (
    campaign_id, 
    target_type, 
    target_id, 
    percentage_value, 
    is_included
) VALUES (1, 'CATEGORY', 2, 5, 1);
```

### Case 3: Tạo voucher code

```sql
INSERT INTO vouchers (
    code,
    name,
    description,
    discount_type,
    value,
    max_discount,
    min_order_value,
    max_uses,
    max_uses_per_user,
    start_date,
    end_date,
    campaign_id,
    applicable_to,
    user_type,
    is_active,
    priority
) VALUES (
    'TET2024',
    N'Voucher Tết 2024',
    N'Giảm 15% tối đa 1 triệu cho đơn từ 2 triệu',
    'percentage',
    15.00,
    1000000.00,
    2000000.00,
    100,
    1,
    '2024-01-20 00:00:00',
    '2024-02-15 23:59:59',
    1,
    'ALL',
    'ALL',
    1,
    5
);
```

## 🔍 Query Examples

### Lấy các campaign đang active

```sql
SELECT *
FROM discount_campaigns
WHERE is_active = 1
  AND GETDATE() BETWEEN start_date AND end_date
ORDER BY priority DESC;
```

### Lấy tất cả discount targets của một campaign

```sql
SELECT 
    dt.*,
    CASE 
        WHEN dt.target_type = 'PRODUCT' THEN p.name
        WHEN dt.target_type = 'VARIANT' THEN v.nameVariants
        WHEN dt.target_type = 'CATEGORY' THEN c.name
    END AS target_name
FROM discount_targets dt
LEFT JOIN products p ON dt.target_type = 'PRODUCT' AND dt.target_id = p.id
LEFT JOIN product_variants v ON dt.target_type = 'VARIANT' AND dt.target_id = v.id
LEFT JOIN categories c ON dt.target_type = 'CATEGORY' AND dt.target_id = c.id
WHERE dt.campaign_id = 1 AND dt.is_included = 1;
```

### Tìm voucher áp dụng được

```sql
SELECT *
FROM vouchers
WHERE code = 'TET2024'
  AND is_active = 1
  AND GETDATE() BETWEEN start_date AND end_date
  AND (max_uses IS NULL OR total_uses < max_uses);
```

### Tính giảm giá cho một sản phẩm

```sql
SELECT TOP 1
    dc.name AS campaign_name,
    dt.percentage_value,
    dt.fixed_discount_value,
    dc.tiered_rules,
    dc.buy_x_get_y_rules
FROM discount_targets dt
INNER JOIN discount_campaigns dc ON dt.campaign_id = dc.id
WHERE dt.target_type = 'PRODUCT'
  AND dt.target_id = 5
  AND dt.is_included = 1
  AND dc.is_active = 1
  AND GETDATE() BETWEEN dc.start_date AND dc.end_date
ORDER BY dc.priority DESC, dt.percentage_value DESC;
```

## 🎯 Thứ tự áp dụng giảm giá

Khi tính toán giảm giá cho đơn hàng:

1. **Campaign Product Discounts** (discount_targets)
   - Áp dụng % giảm cho từng sản phẩm
   
2. **Campaign Advanced Rules** (tiered, buy X get Y, unique products...)
   - Áp dụng các rule từ campaign
   
3. **Voucher Code**
   - Khách nhập mã voucher
   
4. **Order-level Discounts**
   - Giảm cho toàn đơn hàng
   
5. **Free Shipping**
   - Kiểm tra điều kiện free ship

**Priority:** Campaign có `priority` cao hơn được ưu tiên trước

## 📈 Performance Tips

1. **Indexes đã tạo sẵn** - Đã có indexes cho các query thường dùng
2. **JSON caching** - Nên cache parsed JSON trong application
3. **Batch queries** - Load tất cả discounts 1 lần thay vì query nhiều lần
4. **Active campaigns only** - Chỉ load campaigns đang active

## 🔧 Maintenance

### Vô hiệu hóa campaign

```sql
UPDATE discount_campaigns
SET is_active = 0
WHERE id = 1;
```

### Xóa campaign (cascade xóa targets và vouchers)

```sql
-- Xóa targets trước
DELETE FROM discount_targets WHERE campaign_id = 1;

-- Xóa vouchers
DELETE FROM vouchers WHERE campaign_id = 1;

-- Xóa campaign
DELETE FROM discount_campaigns WHERE id = 1;
```

### Update advanced rules

```sql
UPDATE discount_campaigns
SET tiered_rules = N'[
    {"minAmount": 2000000, "discountType": "fixed", "discountValue": 200000},
    {"minAmount": 5000000, "discountType": "percent", "discountValue": 12, "maxDiscount": 1000000}
]',
updated_at = GETDATE()
WHERE id = 1;
```

## 🆘 Troubleshooting

### Issue: JSON không parse được
- Đảm bảo format JSON đúng
- Dùng N'...' prefix cho NVARCHAR
- Test JSON online: jsonlint.com

### Issue: Giảm giá không áp dụng
- Check is_active = 1
- Check start_date/end_date
- Check is_included = 1 trong discount_targets
- Check priority

### Issue: Performance chậm
- Kiểm tra indexes đã tạo
- Use NOLOCK hint cho read-only queries
- Cache parsed JSON

## ✅ Checklist

- [ ] Migration script đã chạy thành công
- [ ] Các columns mới đã được thêm
- [ ] Indexes đã được tạo
- [ ] Models backend đã update
- [ ] Test insert campaign
- [ ] Test insert discount targets
- [ ] Test insert vouchers
- [ ] Test query active campaigns
- [ ] Test áp dụng giảm giá

---

**Database:** MSSQL (SQL Server 2016+)  
**Backend:** Spring Boot 3.x + JPA  
**Version:** 1.0.0  
**Last Updated:** 14/10/2025


