-- Tạo bảng discount_targets
CREATE TABLE IF NOT EXISTS discount_targets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    campaign_id INT NOT NULL,
    target_type VARCHAR(20) NOT NULL, -- 'PRODUCT', 'VARIANT', 'CATEGORY'
    target_id INT NOT NULL,
    percentage_value INT DEFAULT 0,
    is_included BOOLEAN DEFAULT TRUE,
    fixed_discount_value INT,
    min_quantity INT DEFAULT 1,
    max_quantity INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng discount_campaigns
CREATE TABLE IF NOT EXISTS discount_campaigns (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    campaign_type VARCHAR(50) NOT NULL, -- 'SALE', 'HOT', 'FEATURED', 'SEASONAL', 'TET', 'SUMMER'
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    banner_image_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    tiered_rules TEXT, -- JSON
    buy_x_get_y_rules TEXT, -- JSON
    bundle_rules TEXT, -- JSON
    unique_product_rule TEXT, -- JSON
    gift_rules TEXT, -- JSON
    free_shipping_rule TEXT, -- JSON
    min_order_amount INT,
    min_items_count INT,
    order_discount_type VARCHAR(20), -- 'percent' or 'fixed'
    order_discount_value INT,
    order_gift_description TEXT,
    priority INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng product_discount_periods
CREATE TABLE IF NOT EXISTS product_discount_periods (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    discount_period_id BIGINT NOT NULL,
    percentage_value INT NOT NULL,
    included BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Thêm dữ liệu mẫu cho discount_targets
INSERT INTO discount_targets (campaign_id, target_type, target_id, percentage_value, is_included, min_quantity) VALUES
(1, 'PRODUCT', 1, 10, TRUE, 1),
(1, 'PRODUCT', 2, 15, TRUE, 1),
(1, 'VARIANT', 1, 20, TRUE, 2),
(1, 'CATEGORY', 1, 5, TRUE, 1);

-- Thêm dữ liệu mẫu cho discount_campaigns
INSERT INTO discount_campaigns (name, description, campaign_type, start_date, end_date, is_active, min_order_amount, priority) VALUES
('Khuyến mãi Tết 2024', 'Chương trình khuyến mãi lớn nhất năm', 'TET', NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), TRUE, 500000, 1),
('Giảm giá Hè', 'Chương trình giảm giá mùa hè', 'SEASONAL', NOW(), DATE_ADD(NOW(), INTERVAL 60 DAY), TRUE, 300000, 2),
('Black Friday', 'Ngày hội mua sắm lớn nhất', 'SALE', NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), TRUE, 1000000, 3);

-- Thêm dữ liệu mẫu cho product_discount_periods
INSERT INTO product_discount_periods (product_id, discount_period_id, percentage_value, included) VALUES
(1, 1, 10, TRUE),
(2, 1, 15, TRUE),
(3, 1, 20, TRUE),
(1, 2, 5, TRUE),
(2, 2, 8, TRUE);
