-- Migration script để tạo bảng discount_periods và product_discount_periods

-- Tạo bảng discount_periods
CREATE TABLE IF NOT EXISTS discount_periods (
    id BIGSERIAL PRIMARY KEY,
    discount_period_code VARCHAR(50) UNIQUE NOT NULL,
    discount_period_name VARCHAR(255) NOT NULL,
    min_percentage_value INTEGER,
    max_percentage_value INTEGER,
    
    -- Quy tắc áp dụng cho hóa đơn
    order_min_total BIGINT,
    order_min_items INTEGER,
    order_discount_type VARCHAR(20), -- 'percent' or 'fixed'
    order_discount_value BIGINT,
    order_gift_description VARCHAR(500),
    enable_order_rule BOOLEAN DEFAULT FALSE,
    
    -- Các quy tắc nâng cao (JSON)
    tiered_rules JSONB,
    buy_x_get_y_rules JSONB,
    bundle_rules JSONB,
    unique_product_rule JSONB,
    gift_rules JSONB,
    free_shipping_rule JSONB,
    
    -- Thời gian
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status INTEGER DEFAULT 1, -- 1: active, 0: inactive
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng product_discount_periods
CREATE TABLE IF NOT EXISTS product_discount_periods (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    discount_period_id BIGINT NOT NULL,
    percentage_value INTEGER NOT NULL,
    included BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign keys
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_discount_period FOREIGN KEY (discount_period_id) REFERENCES discount_periods(id) ON DELETE CASCADE,
    
    -- Unique constraint để tránh trùng lặp
    CONSTRAINT unique_product_period UNIQUE (product_id, discount_period_id)
);

-- Tạo indexes để tối ưu query
CREATE INDEX idx_discount_periods_code ON discount_periods(discount_period_code);
CREATE INDEX idx_discount_periods_status ON discount_periods(status);
CREATE INDEX idx_discount_periods_time_range ON discount_periods(start_time, end_time);
CREATE INDEX idx_product_discount_periods_product ON product_discount_periods(product_id);
CREATE INDEX idx_product_discount_periods_period ON product_discount_periods(discount_period_id);
CREATE INDEX idx_product_discount_periods_included ON product_discount_periods(included);

-- Thêm comments cho các bảng
COMMENT ON TABLE discount_periods IS 'Bảng lưu trữ các đợt giảm giá với quy tắc nâng cao';
COMMENT ON TABLE product_discount_periods IS 'Bảng ánh xạ sản phẩm và đợt giảm giá';

COMMENT ON COLUMN discount_periods.tiered_rules IS 'JSON lưu quy tắc giảm giá theo bậc thang';
COMMENT ON COLUMN discount_periods.buy_x_get_y_rules IS 'JSON lưu quy tắc mua X tặng Y';
COMMENT ON COLUMN discount_periods.bundle_rules IS 'JSON lưu quy tắc giảm giá combo';
COMMENT ON COLUMN discount_periods.unique_product_rule IS 'JSON lưu quy tắc giảm khi mua nhiều SP khác nhau';
COMMENT ON COLUMN discount_periods.gift_rules IS 'JSON lưu quy tắc quà tặng';
COMMENT ON COLUMN discount_periods.free_shipping_rule IS 'JSON lưu quy tắc miễn phí vận chuyển';

