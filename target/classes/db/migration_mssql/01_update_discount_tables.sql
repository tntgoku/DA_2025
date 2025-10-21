-- Migration script để cập nhật bảng discount_campaigns, discount_targets, vouchers cho MSSQL
-- Chạy script này để thêm các columns mới vào các bảng đã tồn tại

-- ==========================
-- 1. Cập nhật bảng discount_campaigns
-- ==========================

-- Thêm các columns cho advanced rules (JSON stored in NVARCHAR(MAX))
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'tiered_rules')
BEGIN
    ALTER TABLE discount_campaigns ADD tiered_rules NVARCHAR(MAX) NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'buy_x_get_y_rules')
BEGIN
    ALTER TABLE discount_campaigns ADD buy_x_get_y_rules NVARCHAR(MAX) NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'bundle_rules')
BEGIN
    ALTER TABLE discount_campaigns ADD bundle_rules NVARCHAR(MAX) NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'unique_product_rule')
BEGIN
    ALTER TABLE discount_campaigns ADD unique_product_rule NVARCHAR(MAX) NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'gift_rules')
BEGIN
    ALTER TABLE discount_campaigns ADD gift_rules NVARCHAR(MAX) NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'free_shipping_rule')
BEGIN
    ALTER TABLE discount_campaigns ADD free_shipping_rule NVARCHAR(MAX) NULL;
END;

-- Thêm các columns cho order-level rules
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'min_order_amount')
BEGIN
    ALTER TABLE discount_campaigns ADD min_order_amount INT NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'min_items_count')
BEGIN
    ALTER TABLE discount_campaigns ADD min_items_count INT NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'order_discount_type')
BEGIN
    ALTER TABLE discount_campaigns ADD order_discount_type NVARCHAR(20) NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'order_discount_value')
BEGIN
    ALTER TABLE discount_campaigns ADD order_discount_value INT NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'order_gift_description')
BEGIN
    ALTER TABLE discount_campaigns ADD order_gift_description NVARCHAR(500) NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'priority')
BEGIN
    ALTER TABLE discount_campaigns ADD priority INT NOT NULL DEFAULT 0;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_campaigns') AND name = 'updated_at')
BEGIN
    ALTER TABLE discount_campaigns ADD updated_at DATETIME2 NOT NULL DEFAULT GETDATE();
END;

-- ==========================
-- 2. Cập nhật bảng discount_targets
-- ==========================

-- Đổi tên column cũ
IF EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_targets') AND name = 'discount_id')
BEGIN
    EXEC sp_rename 'discount_targets.discount_id', 'campaign_id', 'COLUMN';
END;

IF EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_targets') AND name = 'tartgetId')
BEGIN
    EXEC sp_rename 'discount_targets.tartgetId', 'target_id', 'COLUMN';
END;

-- Thêm các columns mới
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_targets') AND name = 'percentage_value')
BEGIN
    ALTER TABLE discount_targets ADD percentage_value INT NOT NULL DEFAULT 0;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_targets') AND name = 'is_included')
BEGIN
    ALTER TABLE discount_targets ADD is_included BIT NOT NULL DEFAULT 1;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_targets') AND name = 'fixed_discount_value')
BEGIN
    ALTER TABLE discount_targets ADD fixed_discount_value INT NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_targets') AND name = 'min_quantity')
BEGIN
    ALTER TABLE discount_targets ADD min_quantity INT NOT NULL DEFAULT 1;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_targets') AND name = 'max_quantity')
BEGIN
    ALTER TABLE discount_targets ADD max_quantity INT NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_targets') AND name = 'created_at')
BEGIN
    ALTER TABLE discount_targets ADD created_at DATETIME2 NOT NULL DEFAULT GETDATE();
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'discount_targets') AND name = 'updated_at')
BEGIN
    ALTER TABLE discount_targets ADD updated_at DATETIME2 NOT NULL DEFAULT GETDATE();
END;

-- ==========================
-- 3. Cập nhật bảng vouchers
-- ==========================

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'vouchers') AND name = 'applicable_to')
BEGIN
    ALTER TABLE vouchers ADD applicable_to NVARCHAR(20) NOT NULL DEFAULT 'ALL';
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'vouchers') AND name = 'applicable_ids')
BEGIN
    ALTER TABLE vouchers ADD applicable_ids NVARCHAR(MAX) NULL;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'vouchers') AND name = 'user_type')
BEGIN
    ALTER TABLE vouchers ADD user_type NVARCHAR(20) NOT NULL DEFAULT 'ALL';
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'vouchers') AND name = 'priority')
BEGIN
    ALTER TABLE vouchers ADD priority INT NOT NULL DEFAULT 0;
END;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'vouchers') AND name = 'updated_at')
BEGIN
    ALTER TABLE vouchers ADD updated_at DATETIME2 NOT NULL DEFAULT GETDATE();
END;

-- ==========================
-- 4. Tạo indexes để tối ưu query
-- ==========================

-- Indexes cho discount_campaigns
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_campaigns_active_dates')
BEGIN
    CREATE INDEX idx_campaigns_active_dates ON discount_campaigns(is_active, start_date, end_date);
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_campaigns_priority')
BEGIN
    CREATE INDEX idx_campaigns_priority ON discount_campaigns(priority DESC);
END;

-- Indexes cho discount_targets
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_targets_campaign')
BEGIN
    CREATE INDEX idx_targets_campaign ON discount_targets(campaign_id);
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_targets_type_id')
BEGIN
    CREATE INDEX idx_targets_type_id ON discount_targets(target_type, target_id);
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_targets_included')
BEGIN
    CREATE INDEX idx_targets_included ON discount_targets(is_included) WHERE is_included = 1;
END;

-- Indexes cho vouchers
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_vouchers_code')
BEGIN
    CREATE INDEX idx_vouchers_code ON vouchers(code) WHERE is_active = 1;
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_vouchers_campaign')
BEGIN
    CREATE INDEX idx_vouchers_campaign ON vouchers(campaign_id);
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_vouchers_dates')
BEGIN
    CREATE INDEX idx_vouchers_dates ON vouchers(start_date, end_date, is_active);
END;

-- ==========================
-- 5. Add comments (Extended Properties)
-- ==========================

-- Comments cho discount_campaigns
EXEC sp_addextendedproperty 
    @name = N'MS_Description', @value = 'Quy tắc giảm giá theo bậc thang (JSON)',
    @level0type = N'Schema', @level0name = 'dbo',
    @level1type = N'Table',  @level1name = 'discount_campaigns',
    @level2type = N'Column', @level2name = 'tiered_rules';

EXEC sp_addextendedproperty 
    @name = N'MS_Description', @value = 'Quy tắc mua X tặng Y (JSON)',
    @level0type = N'Schema', @level0name = 'dbo',
    @level1type = N'Table',  @level1name = 'discount_campaigns',
    @level2type = N'Column', @level2name = 'buy_x_get_y_rules';

-- Comments cho discount_targets
EXEC sp_addextendedproperty 
    @name = N'MS_Description', @value = 'PRODUCT: toàn bộ sản phẩm, VARIANT: variant cụ thể, CATEGORY: cả danh mục',
    @level0type = N'Schema', @level0name = 'dbo',
    @level1type = N'Table',  @level1name = 'discount_targets',
    @level2type = N'Column', @level2name = 'target_type';

PRINT 'Migration completed successfully!';
GO

