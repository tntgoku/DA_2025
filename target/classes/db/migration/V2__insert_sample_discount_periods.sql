-- Script thêm dữ liệu mẫu cho discount periods

-- Thêm đợt giảm giá Tết 2024
INSERT INTO discount_periods (
    discount_period_code,
    discount_period_name,
    min_percentage_value,
    max_percentage_value,
    order_min_total,
    order_min_items,
    order_discount_type,
    order_discount_value,
    order_gift_description,
    enable_order_rule,
    tiered_rules,
    free_shipping_rule,
    start_time,
    end_time,
    status
) VALUES (
    'TET2024',
    'Khuyến mãi Tết Nguyên Đán 2024',
    5,
    50,
    500000,
    2,
    'percent',
    10,
    'Tặng lịch tết khi mua từ 2 sản phẩm',
    true,
    '[
        {"minAmount": 1000000, "discountType": "fixed", "discountValue": 100000, "maxDiscount": null},
        {"minAmount": 3000000, "discountType": "fixed", "discountValue": 400000, "maxDiscount": null},
        {"minAmount": 5000000, "discountType": "fixed", "discountValue": 800000, "maxDiscount": null}
    ]'::jsonb,
    '{"minAmount": 500000, "maxShippingDiscount": 30000}'::jsonb,
    '2024-01-20 00:00:00',
    '2024-02-15 23:59:59',
    1
);

-- Thêm đợt giảm giá Hè
INSERT INTO discount_periods (
    discount_period_code,
    discount_period_name,
    min_percentage_value,
    max_percentage_value,
    order_min_total,
    enable_order_rule,
    unique_product_rule,
    gift_rules,
    start_time,
    end_time,
    status
) VALUES (
    'SUMMER2024',
    'Khuyến mãi Hè 2024',
    10,
    30,
    2000000,
    true,
    '{"minUniqueProducts": 3, "discountType": "percent", "discountValue": 15, "maxDiscount": 500000}'::jsonb,
    '[
        {"minAmount": 5000000, "minItems": 0, "gifts": [{"description": "Tai nghe Bluetooth", "estimatedValue": 500000}]},
        {"minAmount": 10000000, "minItems": 0, "gifts": [{"description": "Apple Watch SE", "estimatedValue": 8000000}]}
    ]'::jsonb,
    '2024-06-01 00:00:00',
    '2024-08-31 23:59:59',
    1
);

-- Thêm đợt Mua 2 Tặng 1
INSERT INTO discount_periods (
    discount_period_code,
    discount_period_name,
    min_percentage_value,
    max_percentage_value,
    buy_x_get_y_rules,
    start_time,
    end_time,
    status
) VALUES (
    'BUY2GET1',
    'Mua 2 Tặng 1 Phụ Kiện',
    0,
    100,
    '[
        {
            "buyProductIds": ["1", "2", "3"],
            "buyQuantity": 2,
            "getProductIds": ["10", "11", "12"],
            "getQuantity": 1,
            "getDiscountPercent": 100,
            "maxApplications": 1
        }
    ]'::jsonb,
    '2024-01-01 00:00:00',
    '2024-12-31 23:59:59',
    1
);

COMMENT ON TABLE discount_periods IS 'Các đợt giảm giá với quy tắc nâng cao. Sử dụng JSONB để lưu trữ linh hoạt';

