-- Insert dữ liệu mẫu cho hệ thống discount nâng cao
-- MSSQL version

-- ==========================
-- 1. Insert Sample Campaigns
-- ==========================

-- Campaign 1: Khuyến mãi Tết 2024
INSERT INTO discount_campaigns (
    name, 
    description,
    campaign_type,
    start_date,
    end_date,
    banner_image_url,
    is_active,
    tiered_rules,
    buy_x_get_y_rules,
    unique_product_rule,
    gift_rules,
    free_shipping_rule,
    min_order_amount,
    min_items_count,
    order_discount_type,
    order_discount_value,
    order_gift_description,
    priority,
    created_at,
    updated_at
) VALUES (
    N'Khuyến mãi Tết Nguyên Đán 2024',
    N'Giảm giá khủng mừng Tết Nguyên Đán - Mua nhiều giảm nhiều!',
    'TET',
    '2024-01-20 00:00:00',
    '2024-02-15 23:59:59',
    '/images/banners/tet2024.jpg',
    1,
    N'[
        {"minAmount": 1000000, "discountType": "fixed", "discountValue": 100000, "maxDiscount": null},
        {"minAmount": 3000000, "discountType": "fixed", "discountValue": 400000, "maxDiscount": null},
        {"minAmount": 5000000, "discountType": "percent", "discountValue": 10, "maxDiscount": 800000}
    ]',
    N'[
        {
            "buyProductIds": ["1", "2", "3"],
            "buyQuantity": 2,
            "getProductIds": ["10", "11", "12"],
            "getQuantity": 1,
            "getDiscountPercent": 100,
            "maxApplications": 1
        }
    ]',
    N'{
        "minUniqueProducts": 3,
        "discountType": "percent",
        "discountValue": 15,
        "maxDiscount": 500000
    }',
    N'[
        {
            "minAmount": 5000000,
            "minItems": 0,
            "gifts": [
                {"description": "Lịch tết cao cấp 2024", "estimatedValue": 150000}
            ]
        },
        {
            "minAmount": 10000000,
            "minItems": 0,
            "gifts": [
                {"description": "Túi xách tết sang trọng", "estimatedValue": 500000}
            ]
        }
    ]',
    N'{
        "minAmount": 500000,
        "maxShippingDiscount": 30000
    }',
    500000,
    2,
    'fixed',
    50000,
    N'Tặng lịch tết khi mua từ 2 sản phẩm trở lên',
    10,
    GETDATE(),
    GETDATE()
);

-- Campaign 2: Giảm giá Hè 2024
INSERT INTO discount_campaigns (
    name, 
    description,
    campaign_type,
    start_date,
    end_date,
    banner_image_url,
    is_active,
    tiered_rules,
    unique_product_rule,
    gift_rules,
    free_shipping_rule,
    min_order_amount,
    priority,
    created_at,
    updated_at
) VALUES (
    N'Khuyến mãi Hè - Mát Lạnh Giá Sốc',
    N'Giảm giá cực sốc mùa hè - Càng mua nhiều càng giảm nhiều!',
    'SUMMER',
    '2024-06-01 00:00:00',
    '2024-08-31 23:59:59',
    '/images/banners/summer2024.jpg',
    1,
    N'[
        {"minAmount": 2000000, "discountType": "percent", "discountValue": 5, "maxDiscount": 200000},
        {"minAmount": 5000000, "discountType": "percent", "discountValue": 10, "maxDiscount": 700000},
        {"minAmount": 10000000, "discountType": "percent", "discountValue": 15, "maxDiscount": 2000000}
    ]',
    N'{
        "minUniqueProducts": 2,
        "discountType": "percent",
        "discountValue": 10,
        "maxDiscount": 300000
    }',
    N'[
        {
            "minAmount": 7000000,
            "minItems": 0,
            "gifts": [
                {"description": "Tai nghe Bluetooth cao cấp", "estimatedValue": 800000}
            ]
        }
    ]',
    N'{
        "minAmount": 300000,
        "maxShippingDiscount": 25000
    }',
    300000,
    5,
    GETDATE(),
    GETDATE()
);

-- Campaign 3: Black Friday 2024
INSERT INTO discount_campaigns (
    name, 
    description,
    campaign_type,
    start_date,
    end_date,
    banner_image_url,
    is_active,
    tiered_rules,
    bundle_rules,
    priority,
    created_at,
    updated_at
) VALUES (
    N'Black Friday 2024 - Siêu Sale',
    N'Black Friday - Giảm giá lên đến 50%!',
    'HOT',
    '2024-11-20 00:00:00',
    '2024-11-30 23:59:59',
    '/images/banners/blackfriday2024.jpg',
    1,
    N'[
        {"minAmount": 1000000, "discountType": "percent", "discountValue": 10, "maxDiscount": null},
        {"minAmount": 5000000, "discountType": "percent", "discountValue": 20, "maxDiscount": null},
        {"minAmount": 10000000, "discountType": "percent", "discountValue": 30, "maxDiscount": 5000000}
    ]',
    N'[
        {
            "requiredProducts": [
                {"productId": "1", "minQuantity": 1},
                {"productId": "5", "minQuantity": 1}
            ],
            "discountType": "percent",
            "discountValue": 25,
            "maxDiscount": 2000000
        }
    ]',
    15,
    GETDATE(),
    GETDATE()
);

PRINT 'Inserted 3 sample campaigns';

-- ==========================
-- 2. Insert Sample Discount Targets
-- ==========================
-- (Chỉ insert nếu có products trong DB)

-- Giả sử product_id = 1, 2, 3 là iPhone; variant_id = 10, 11, 12 là các variant khác nhau
-- Bạn cần thay đổi IDs phù hợp với database thực tế

/*
-- Ví dụ: Giảm 10% cho iPhone trong campaign Tết
INSERT INTO discount_targets (campaign_id, target_type, target_id, percentage_value, is_included, min_quantity, created_at, updated_at)
VALUES (1, 'PRODUCT', 1, 10, 1, 1, GETDATE(), GETDATE());

INSERT INTO discount_targets (campaign_id, target_type, target_id, percentage_value, is_included, min_quantity, created_at, updated_at)
VALUES (1, 'PRODUCT', 2, 15, 1, 1, GETDATE(), GETDATE());

INSERT INTO discount_targets (campaign_id, target_type, target_id, percentage_value, is_included, min_quantity, created_at, updated_at)
VALUES (1, 'PRODUCT', 3, 12, 1, 1, GETDATE(), GETDATE());

-- Giảm 20% cho variant cụ thể
INSERT INTO discount_targets (campaign_id, target_type, target_id, percentage_value, is_included, min_quantity, created_at, updated_at)
VALUES (2, 'VARIANT', 10, 20, 1, 1, GETDATE(), GETDATE());

-- Giảm 5% cho toàn danh mục (category_id = 1)
INSERT INTO discount_targets (campaign_id, target_type, target_id, percentage_value, is_included, min_quantity, created_at, updated_at)
VALUES (3, 'CATEGORY', 1, 5, 1, 1, GETDATE(), GETDATE());

PRINT 'Inserted sample discount targets';
*/

-- ==========================
-- 3. Insert Sample Vouchers
-- ==========================

-- Voucher 1: Tết 2024
INSERT INTO vouchers (
    code,
    name,
    description,
    discount_type,
    value,
    max_discount,
    min_order_value,
    total_uses,
    max_uses,
    max_uses_per_user,
    start_date,
    end_date,
    is_active,
    campaign_id,
    applicable_to,
    user_type,
    priority,
    created_at,
    updated_at
) VALUES (
    'TET2024',
    N'Voucher Tết 2024',
    N'Giảm 15% tối đa 1 triệu cho đơn từ 2 triệu',
    'percentage',
    15.00,
    1000000.00,
    2000000.00,
    0,
    100,
    1,
    '2024-01-20 00:00:00',
    '2024-02-15 23:59:59',
    1,
    1,
    'ALL',
    'ALL',
    10,
    GETDATE(),
    GETDATE()
);

-- Voucher 2: Hè 2024
INSERT INTO vouchers (
    code,
    name,
    description,
    discount_type,
    value,
    max_discount,
    min_order_value,
    total_uses,
    max_uses,
    max_uses_per_user,
    start_date,
    end_date,
    is_active,
    campaign_id,
    applicable_to,
    user_type,
    priority,
    created_at,
    updated_at
) VALUES (
    'SUMMER10',
    N'Voucher Hè - Giảm 10%',
    N'Giảm 10% tối đa 500k cho đơn từ 1 triệu',
    'percentage',
    10.00,
    500000.00,
    1000000.00,
    0,
    200,
    2,
    '2024-06-01 00:00:00',
    '2024-08-31 23:59:59',
    1,
    2,
    'ALL',
    'ALL',
    5,
    GETDATE(),
    GETDATE()
);

-- Voucher 3: Giảm cố định 200k
INSERT INTO vouchers (
    code,
    name,
    description,
    discount_type,
    value,
    max_discount,
    min_order_value,
    total_uses,
    max_uses,
    max_uses_per_user,
    start_date,
    end_date,
    is_active,
    campaign_id,
    applicable_to,
    user_type,
    priority,
    created_at,
    updated_at
) VALUES (
    'SAVE200K',
    N'Giảm ngay 200K',
    N'Giảm ngay 200k cho đơn từ 3 triệu',
    'fixed_amount',
    200000.00,
    NULL,
    3000000.00,
    0,
    50,
    1,
    '2024-01-01 00:00:00',
    '2024-12-31 23:59:59',
    1,
    1,
    'ALL',
    'ALL',
    8,
    GETDATE(),
    GETDATE()
);

-- Voucher 4: Dành cho khách VIP
INSERT INTO vouchers (
    code,
    name,
    description,
    discount_type,
    value,
    max_discount,
    min_order_value,
    total_uses,
    max_uses,
    max_uses_per_user,
    start_date,
    end_date,
    is_active,
    campaign_id,
    applicable_to,
    user_type,
    priority,
    created_at,
    updated_at
) VALUES (
    'VIP20',
    N'VIP - Giảm 20%',
    N'Ưu đãi đặc biệt cho khách hàng VIP - Giảm 20%',
    'percentage',
    20.00,
    2000000.00,
    5000000.00,
    0,
    NULL, -- Không giới hạn số lần sử dụng
    5,
    '2024-01-01 00:00:00',
    '2024-12-31 23:59:59',
    1,
    NULL,
    'ALL',
    'VIP',
    20,
    GETDATE(),
    GETDATE()
);

-- Voucher 5: Dành cho khách mới
INSERT INTO vouchers (
    code,
    name,
    description,
    discount_type,
    value,
    max_discount,
    min_order_value,
    total_uses,
    max_uses,
    max_uses_per_user,
    start_date,
    end_date,
    is_active,
    campaign_id,
    applicable_to,
    user_type,
    priority,
    created_at,
    updated_at
) VALUES (
    'NEWUSER50',
    N'Khách mới - Giảm 50K',
    N'Chào mừng khách hàng mới - Giảm ngay 50k',
    'fixed_amount',
    50000.00,
    NULL,
    300000.00,
    0,
    1000,
    1,
    '2024-01-01 00:00:00',
    '2024-12-31 23:59:59',
    1,
    NULL,
    'ALL',
    'NEW',
    15,
    GETDATE(),
    GETDATE()
);

PRINT 'Inserted 5 sample vouchers';

-- ==========================
-- 4. Verify Data
-- ==========================

-- Kiểm tra campaigns
SELECT 
    id, 
    name, 
    campaign_type, 
    is_active,
    priority,
    CASE 
        WHEN tiered_rules IS NOT NULL THEN 'YES'
        ELSE 'NO'
    END AS has_tiered_rules,
    CASE 
        WHEN buy_x_get_y_rules IS NOT NULL THEN 'YES'
        ELSE 'NO'
    END AS has_buy_x_get_y
FROM discount_campaigns
ORDER BY priority DESC;

-- Kiểm tra vouchers
SELECT 
    code,
    name,
    discount_type,
    value,
    min_order_value,
    is_active,
    user_type,
    priority
FROM vouchers
ORDER BY priority DESC;

PRINT 'Sample data inserted successfully!';
PRINT 'Total campaigns: ' + CAST((SELECT COUNT(*) FROM discount_campaigns) AS VARCHAR);
PRINT 'Total vouchers: ' + CAST((SELECT COUNT(*) FROM vouchers) AS VARCHAR);

GO

