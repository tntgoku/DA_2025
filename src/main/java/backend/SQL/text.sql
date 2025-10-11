
-- === === === === === === === === === === === === === === ===
--DANH MỤC & SẢN PHẨM
    -- === === === === === === === === === === === === === === ===
CREATE TABLE categories(
        id INT IDENTITY(1, 1) PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        slug NVARCHAR(255) UNIQUE NOT NULL,
        parent_id INT NULL,
        description NVARCHAR(500),
        image_url NVARCHAR(500),
        display_order INT DEFAULT 0,
        is_active BIT DEFAULT 1,
        created_at DATETIME2 DEFAULT GETDATE(),
        updated_at DATETIME2 DEFAULT GETDATE(),
        FOREIGN KEY(parent_id) REFERENCES categories(id)
    );
	GO
CREATE TABLE products(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    category_id INT NOT NULL,
    product_type NVARCHAR(20) DEFAULT NULL,
    name NVARCHAR(255) NOT NULL,
    slug NVARCHAR(300) UNIQUE NOT NULL,
    description NVARCHAR(MAX),
    brand NVARCHAR(100) NOT NULL,
    model NVARCHAR(100),
    specifications NVARCHAR(MAX),
    is_active BIT DEFAULT 1,
    is_featured BIT DEFAULT 0,
    is_hot BIT DEFAULT 0,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(category_id) REFERENCES categories(id)
);

-- === === === === === === === === === === === === === === ===
--PHIÊN BẢN SẢN PHẨM
-- === === === === === === === === === === === === === === ===
CREATE TABLE product_variants(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    product_id INT NOT NULL,
	name_variants nvarchar(max) ,
    sku NVARCHAR(100) UNIQUE NOT NULL,
    color NVARCHAR(50) NOT NULL,
    color_code NVARCHAR(20),
    storage NVARCHAR(50) NOT NULL,
    ram NVARCHAR(20),
    region_code NVARCHAR(20),
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
);


--alter table product_variants
--add name_variants nvarchar(max) 
-- === === === === === === === === === === === === === === ===
--THÔNG SỐ KỸ THUẬT
-- === === === === === === === === === === === === === === ===
CREATE TABLE spec_groups(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    description NVARCHAR(500),
    display_order INT DEFAULT 0,
    is_active BIT DEFAULT 1
);

CREATE TABLE product_specs(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    product_id INT NOT NULL,
    spec_group_id INT NOT NULL,
    spec_name NVARCHAR(255) NOT NULL,
    spec_value NVARCHAR(255) NOT NULL,
    spec_unit NVARCHAR(50) NULL,
    display_order INT DEFAULT 0,
    FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY(spec_group_id) REFERENCES spec_groups(id)
);

-- === === === === === === === === === === === === === === ===
--TÌNH TRẠNG & NGUỒN HÀNG
    -- === === === === === === === === === === === === === === ===
    CREATE TABLE product_conditions(
        id INT IDENTITY(1, 1) PRIMARY KEY,
        condition_name NVARCHAR(100) NOT NULL,
        description NVARCHAR(255),
        discount_rate DECIMAL(5, 2) DEFAULT 0,
        is_active BIT DEFAULT 1
    );

CREATE TABLE suppliers(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20),
    email NVARCHAR(100),
    address NVARCHAR(MAX),
    supplier_type NVARCHAR(50) NOT NULL, --'OFFICIAL', 'DISTRIBUTOR', 'WHOLESALER'
    tax_code NVARCHAR(50),
    reliability_rating INT DEFAULT 5,
    payment_terms NVARCHAR(MAX),
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE()
);

-- === === === === === === === === === === === === === === ===
--TÀI KHOẢN & KHÁCH HÀNG
    -- === === === === === === === === === === === === === === ===
    CREATE TABLE roles(
        id INT IDENTITY(1, 1) PRIMARY KEY,
        role_name NVARCHAR(50) NOT NULL UNIQUE,
        description NVARCHAR(255),
    );

CREATE TABLE accounts(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    username NVARCHAR(100) UNIQUE NOT NULL,
    password_hash NVARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    is_active BIT DEFAULT 1,
    last_login DATETIME2 NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(role_id) REFERENCES roles(id)
);

CREATE TABLE users(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    account_id INT NULL,
    full_name NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20) UNIQUE NOT NULL,
    email NVARCHAR(100) UNIQUE,
    email_verified BIT DEFAULT 0,
    phone_verified BIT DEFAULT 0,
    date_of_birth DATE NULL,
    gender NVARCHAR(10),
    address NVARCHAR(MAX),
    total_orders INT DEFAULT 0,
    total_spent DECIMAL(18, 2) DEFAULT 0,
    notes NVARCHAR(MAX),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(account_id) REFERENCES accounts(id) ON DELETE SET NULL
);
-- === === === === === === === === === === === === === === ===
-- ĐỊA CHỈ GIAO HÀNG
-- === === === === === === === === === === === === === === ===
CREATE TABLE shipping_addresses (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    recipient_name NVARCHAR(255),
    phone NVARCHAR(20) NOT NULL,
    address_line1 NVARCHAR(255),
    address_line2 NVARCHAR(255),
    city NVARCHAR(100),
    state NVARCHAR(100),
    postal_code NVARCHAR(20),
    is_default BIT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- === === === === === === === === === === === === === === ===
--NGUỒN HÀNG & QUẢN LÝ KHO
    -- === === === === === === === === === === === === === === ===
    CREATE TABLE product_sources(
        id INT IDENTITY(1, 1) PRIMARY KEY,
        source_type NVARCHAR(50) NOT NULL CHECK(source_type IN('SUPPLIER', 'CUSTOMER_TRADE_IN', 'INTERNAL')),
        supplier_id INT NULL,
        customer_id INT NULL,
        source_date DATETIME2 DEFAULT GETDATE(),
        reference_number NVARCHAR(100),
        total_amount DECIMAL(18, 2),
        notes NVARCHAR(MAX),
        created_at DATETIME2 DEFAULT GETDATE(),
        FOREIGN KEY(supplier_id) REFERENCES suppliers(id),
        FOREIGN KEY(customer_id) REFERENCES users(id)
    );

CREATE TABLE inventory_items(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    product_variant_id INT NOT NULL,
    condition_id INT  NULL,
    source_id INT  NULL,
    imei NVARCHAR(50) DEFAULT NULL,
    serial_number NVARCHAR(100),
    cost_price DECIMAL(18, 2) NOT NULL,
    sale_price DECIMAL(18, 2) NOT NULL,
    list_price DECIMAL(18, 2),
    status NVARCHAR(50) DEFAULT 'available'
    CHECK(status IN('available', 'sold', 'reserved', 'defective', 'returned')),
    position NVARCHAR(255),
    import_date DATETIME2 DEFAULT GETDATE(),
    sold_date DATETIME2 NULL,
    warranty_months INT,
    device_condition_notes NVARCHAR(MAX),
    previous_owner_info NVARCHAR(MAX),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(product_variant_id) REFERENCES product_variants(id),
    FOREIGN KEY(condition_id) REFERENCES product_conditions(id),
    FOREIGN KEY(source_id) REFERENCES product_sources(id)
);


-- === === === === === === === === === === === === === === ===
--KHUYẾN MÃI & VOUCHER
    -- === === === === === === === === === === === === === === ===
    CREATE TABLE discount_campaigns(
        id INT IDENTITY(1, 1) PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        description NVARCHAR(MAX),
        campaign_type NVARCHAR(50) NOT NULL, --'SALE', 'HOT', 'FEATURED', 'SEASONAL'
        start_date DATETIME2 NOT NULL,
        end_date DATETIME2 NOT NULL,
        banner_image_url NVARCHAR(500),
        is_active BIT DEFAULT 1,
        created_at DATETIME2 DEFAULT GETDATE()
    );

CREATE TABLE discounts(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    campaign_id INT NULL,
    discount_type NVARCHAR(50) NOT NULL CHECK(discount_type IN('percentage', 'fixed_amount')),
    value DECIMAL(10, 2) NOT NULL,
    max_discount DECIMAL(10, 2) NULL,
    min_order_value DECIMAL(10, 2) NULL,
    is_active BIT DEFAULT 1,
    start_date DATETIME2 DEFAULT GETDATE(),
    end_date DATETIME2 NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(campaign_id) REFERENCES discount_campaigns(id)
);

CREATE TABLE discount_targets(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    discount_id INT NOT NULL,
    target_type NVARCHAR(50) NOT NULL CHECK(target_type IN('product', 'category', 'variant')),
    target_id INT NULL,
    FOREIGN KEY(discount_id) REFERENCES discounts(id) ON DELETE CASCADE
);
drop table discount_targets
CREATE TABLE vouchers(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    code NVARCHAR(50) UNIQUE NOT NULL,
    name NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    discount_type NVARCHAR(50) NOT NULL CHECK(discount_type IN('percentage', 'fixed_amount')),
    value DECIMAL(10, 2) NOT NULL,
    max_discount DECIMAL(10, 2) NULL,
    min_order_value DECIMAL(10, 2) NULL,
    total_uses INT DEFAULT 0,
    max_uses INT NULL,
    max_uses_per_user INT NULL,
    start_date DATETIME2 NOT NULL,
    end_date DATETIME2 NOT NULL,
    is_active BIT DEFAULT 1,
    campaign_id INT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(campaign_id) REFERENCES discount_campaigns(id)
);

CREATE TABLE campaign_products(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    campaign_id INT NOT NULL,
    product_id INT NOT NULL,
    display_order INT DEFAULT 0,
    special_note NVARCHAR(255),
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(campaign_id) REFERENCES discount_campaigns(id),
    FOREIGN KEY(product_id) REFERENCES products(id)
);

-- === === === === === === === === === === === === === === ===
--TAG SẢN PHẨM & HIỂN THỊ
    -- === === === === === === === === === === === === === === ===
    CREATE TABLE product_tags(
        id INT IDENTITY(1, 1) PRIMARY KEY,
        tag_name NVARCHAR(50) NOT NULL UNIQUE,
        display_name NVARCHAR(100),
        description NVARCHAR(255),
        bg_color NVARCHAR(20),
        text_color NVARCHAR(20),
        is_active BIT DEFAULT 1
    );

CREATE TABLE product_tag_map(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    product_id INT NOT NULL,
    tag_id INT NOT NULL,
    start_date DATETIME2 NULL,
    end_date DATETIME2 NULL,
    display_order INT DEFAULT 0,
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY(tag_id) REFERENCES product_tags(id)
);

-- === === === === === === === === === === === === === === ===
--HÌNH ẢNH SẢN PHẨM
-- === === === === === === === === === === === === === === ===
CREATE TABLE product_images(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    product_variant_id INT  NULL,
	product_id int null,
    image_url NVARCHAR(500) NOT NULL,
    image_type NVARCHAR(50) DEFAULT 'gallery',
    display_order INT DEFAULT 0,
    alt_img NVARCHAR(MAX),
    is_primary BIT DEFAULT 0,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(product_variant_id) REFERENCES product_variants(id) ON DELETE CASCADE,
	FOREIGN KEY(product_id) REFERENCES products(id)
);

CREATE TABLE banners(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    title NVARCHAR(255) NOT NULL,
    image_url NVARCHAR(500) NOT NULL,
    thumbnail_url NVARCHAR(500),
    target_type NVARCHAR(50), --'product', 'category', 'url', 'campaign'
    target_id INT,
    target_url NVARCHAR(500),
    banner_type NVARCHAR(50) NOT NULL, --'main', 'sidebar', 'popup', 'category'
    display_order INT DEFAULT 0,
    start_date DATETIME2,
    end_date DATETIME2,
    is_active BIT DEFAULT 1,
    clicks INT DEFAULT 0,
    created_at DATETIME2 DEFAULT GETDATE()
);

-- === === === === === === === === === === === === === === ===
--ĐƠN HÀNG & BÁN HÀNG
    -- === === === === === === === === === === === === === === ===
CREATE TABLE orders(
        id INT IDENTITY(1, 1) PRIMARY KEY,
        order_code NVARCHAR(50) UNIQUE NOT NULL,
        customer_id INT NOT NULL,
        customer_name NVARCHAR(255),
        customer_phone NVARCHAR(20),
        customer_email NVARCHAR(100),
        customer_address NVARCHAR(MAX),
        order_status NVARCHAR(50) DEFAULT 'pending'
        CHECK(order_status IN('pending', 'confirmed', 'processing', 'shipped', 'delivered', 'cancelled', 'returned')),
        payment_status NVARCHAR(50) DEFAULT 'unpaid'
        CHECK(payment_status IN('unpaid', 'paid', 'partial', 'refunded')),
        payment_method NVARCHAR(50),
        subtotal_amount DECIMAL(18, 2) NOT NULL,
        discount_amount DECIMAL(18, 2) DEFAULT 0,
        shipping_fee DECIMAL(18, 2) DEFAULT 0,
        tax_amount DECIMAL(18, 2) DEFAULT 0,
        total_amount DECIMAL(18, 2) NOT NULL,
        amount_paid DECIMAL(18, 2) DEFAULT 0,
        voucher_id INT NULL,
        voucher_discount DECIMAL(18, 2) DEFAULT 0,
        shipping_address NVARCHAR(MAX),
        shipping_method NVARCHAR(100),
        tracking_number NVARCHAR(100),
        notes NVARCHAR(MAX),
        created_by INT,
        created_at DATETIME2 DEFAULT GETDATE(),
        updated_at DATETIME2 DEFAULT GETDATE(),
        FOREIGN KEY(customer_id) REFERENCES users(id),
        FOREIGN KEY(voucher_id) REFERENCES vouchers(id)
    );

CREATE TABLE order_items(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    order_id INT NOT NULL,
    inventory_item_id INT  NULL,
	variant_id int null,
    product_name NVARCHAR(255) NOT NULL,
    variant_attributes NVARCHAR(500) NOT NULL,
    sku NVARCHAR(100) NOT NULL,
    condition_type NVARCHAR(50) NOT NULL,
    imei NVARCHAR(50), --Dùng cho sản phẩm cũ quản lý = EMEIL <= > inventory_item_id unit_cost_price DECIMAL(18, 2) NOT NULL,
    unit_sale_price DECIMAL(18, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    discount_amount DECIMAL(18, 2) DEFAULT 0,
    total_price DECIMAL(18, 2) NOT NULL,
    warranty_months INT,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY(inventory_item_id) REFERENCES inventory_items(id),
	FOREIGN KEY(variant_id) REFERENCES  product_variants(id)
);

-- === === === === === === === === === === === === === === ===
--TRẢ GÓP
-- === === === === === === === === === === === === === === ===
CREATE TABLE installment_plans(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    bank_name NVARCHAR(100),
    months INT NOT NULL,
    interest_rate DECIMAL(5, 2) NOT NULL,
    min_amount DECIMAL(18, 2),
    max_amount DECIMAL(18, 2),
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE()
);

-- === === === === === === === === === === === === === === ===
--DỊCH VỤ SỬA CHỮA
-- === === === === === === === === === === === === === === ===

CREATE TABLE repair_services(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    category_id INT NOT NULL,
    estimated_time INT, --thời gian ước tính(phút) warranty_days INT, --bảo hành dịch vụ(ngày) base_price DECIMAL(18, 2) NOT NULL, --giá dịch vụ is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(category_id) REFERENCES categories(id)
);
CREATE TABLE repair_orders(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    repair_code NVARCHAR(50) UNIQUE NOT NULL,
    customer_id INT NOT NULL,
    device_type NVARCHAR(100) NOT NULL,
    device_model NVARCHAR(100),
    serial_number NVARCHAR(100),
    device_condition NVARCHAR(MAX),
    problem_description NVARCHAR(MAX),
    diagnostic_notes NVARCHAR(MAX),
    repair_status NVARCHAR(50) DEFAULT 'received',
    estimated_cost DECIMAL(18, 2),
    final_cost DECIMAL(18, 2),
    estimated_completion_date DATETIME2,
    actual_completion_date DATETIME2,
    technician_notes NVARCHAR(MAX),
    parts_used NVARCHAR(MAX),
    warranty_until DATETIME2,
    created_by INT,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(customer_id) REFERENCES users(id),
);

CREATE TABLE repair_order_services(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    repair_order_id INT NOT NULL,
    repair_service_id INT NULL,
    quantity INT DEFAULT 1,
    unit_price DECIMAL(18, 2) NOT NULL,
    total_price DECIMAL(18, 2) NOT NULL,
    notes NVARCHAR(MAX),
    FOREIGN KEY(repair_order_id) REFERENCES repair_orders(id) ON DELETE CASCADE,
    FOREIGN KEY(repair_service_id) REFERENCES repair_services(id)
);
use trungghieu
go
select * from  discount_campaigns
go
select * from vouchers
go
select * from installment_plans

-- === DISCOUNT CAMPAIGNS & VOUCHERS ===
INSERT INTO discount_campaigns (name, description, campaign_type, start_date, end_date, is_active) 
VALUES 
(N'Khuyến mãi tháng 5', N'Chương trình khuyến mãi đặc biệt tháng 5', 'SEASONAL', '2024-05-01', '2024-05-31', 1),
(N'Hot Sale 2024', N'Chương trình giảm giá lớn nhất năm', 'SALE', '2024-06-01', '2024-06-30', 1);

INSERT INTO vouchers (code, name, description, discount_type, value, max_discount, min_order_value, max_uses, start_date, end_date, is_active, campaign_id)
VALUES 
('GIAM50K', N'Giảm 50K cho đơn từ 1TR', N'Voucher giảm 50K cho đơn hàng từ 1.000.000đ', 'fixed_amount', 50000, 50000, 1000000, 100, '2024-05-01', '2024-12-31', 1, 1),
('GIAM10%', N'Giảm 10% tối đa 100K', N'Voucher giảm 10% tối đa 100.000đ', 'percentage', 10, 100000, 500000, 50, '2024-05-01', '2024-12-31', 1, 2);
select * from users
-- === ORDERS ===
INSERT INTO orders (
    order_code, customer_id, customer_name, customer_phone, customer_email, 
    order_status, payment_status, payment_method, 
    subtotal_amount, discount_amount, shipping_fee, total_amount, amount_paid,
    voucher_id, voucher_discount, shipping_address, shipping_method, created_by
) VALUES 
-- Đơn hàng 1: Khách đã đăng ký, đã thanh toán
('ORD001', 3, N'Lê Văn C (Khách hàng ĐK)', '0903333333', 'customer1@mail.com',
 'delivered', 'paid', 'bank_transfer',
 24990000, 0, 0, 24990000, 24990000,
 NULL, 0, N'123 Đường Lê Lợi, Quận 1, TP.HCM', N'Giao hàng nhanh', 2),

-- Đơn hàng 2: Khách vãng lai, chưa thanh toán
('ORD002', 4, N'Phạm Thị D (Khách hàng Vãng lai)', '0904444444', 'guest@mail.com',
 'confirmed', 'unpaid', 'cod',
 15990000, 50000, 30000, 15970000, 0,
 1, 50000, N'456 Đường Nguyễn Huệ, Quận 1, TP.HCM', N'Giao hàng tiêu chuẩn', 2),

-- Đơn hàng 3: Đơn hủy
('ORD003', 3, N'Lê Văn C (Khách hàng ĐK)', '0903333333', 'customer1@mail.com',
 'cancelled', 'unpaid', 'bank_transfer',
 1290000, 0, 0, 1290000, 0,
 NULL, 0, N'123 Đường Lê Lợi, Quận 1, TP.HCM', N'Giao hàng nhanh', 2);
 select * from order_items
-- === ORDER ITEMS ===
INSERT INTO order_items (
    order_id, inventory_item_id, variant_id,
    product_name, variant_attributes, sku, condition_type, imei,
    unit_cost_price, unit_sale_price, quantity, discount_amount, total_price, warranty_months
) VALUES 
-- Order 1: iPhone 15 Pro Mới
(1, 1, 1,
 N'iPhone 15 Pro', N'Màu: Titan Đen, Storage: 256GB', 'IP15P-256-BLK', N'Mới 100% (New Sealed)', '35123400000001',
 22000000, 24990000, 1, 0, 24990000, 12),

-- Order 2: Samsung S22 Ultra Cũ + Tai nghe
(2, 2, 3,
 N'Samsung Galaxy S22 Ultra', N'Màu: Đen, Storage: 512GB', 'S22U-512-BLK', N'Likenew 99%', '35987600000002',
 12000000, 15990000, 1, 50000, 15940000, 6),

-- Order 3: Tai nghe (đơn hủy)
(3, 4, 4,
 N'Tai nghe Air Pro V2', N'Màu: Trắng', 'APV2-WHT', N'Mới 100% (New Sealed)', NULL,
 500000, 1290000, 1, 0, 1290000, 3);

-- Cập nhật trạng thái inventory cho các sản phẩm đã bán
UPDATE inventory_items SET status = 'sold', sold_date = GETDATE() 
WHERE id IN (1, 2, 4);
INSERT INTO installment_plans (name, bank_name, months, interest_rate, min_amount, max_amount, is_active)
VALUES 
(N'Trả góp 0% 6 tháng', 'VPBank', 6, 0, 5000000, 50000000, 1),
(N'Trả góp 3 tháng', 'Home Credit', 3, 1.5, 3000000, 30000000, 1);

-- === DISCOUNTS CHO SẢN PHẨM & BIẾN THỂ ===
INSERT INTO discounts (campaign_id, discount_type, value, max_discount, min_order_value, is_active, start_date, end_date)
VALUES 
-- Giảm giá cho sản phẩm iPhone 15 Pro
(1, 'percentage', 5, 1000000, 0, 1, '2024-05-01', '2024-05-31'),
-- Giảm giá cho biến thể Samsung S22 Ultra màu đen
(2, 'fixed_amount', 500000, 500000, 0, 1, '2024-06-01', '2024-06-30'),
-- Giảm giá cho toàn bộ danh mục phụ kiện
(1, 'percentage', 10, 200000, 0, 1, '2024-05-01', '2024-05-31');
go
select * from campaign_products
-- === DISCOUNT TARGETS - ÁP DỤNG GIẢM GIÁ CHO TỪNG ĐỐI TƯỢNG ===
INSERT INTO discount_targets (discount_id, target_type, target_id) VALUES
-- Discount 1: Áp dụng cho sản phẩm iPhone 15 Pro (product_id = 1)
(1, 'product', 1),
-- Discount 2: Áp dụng cho biến thể Samsung S22 Ultra màu đen (variant_id = 3)
(2, 'variant', 3),
-- Discount 3: Áp dụng cho toàn bộ danh mục Phụ kiện (category_id = 3)
(3, 'category', 3);
select dt.*,p.* from discount_targets dt left join products p on dt.target_id=p.id
SELECT 
    d.id AS discount_id,
    d.discount_type,
    d.value,
    dt.target_type,
    dt.target_id,
    CASE 
        WHEN dt.target_type = 'product' THEN p.name
        WHEN dt.target_type = 'variant' THEN pv.name_variants
        WHEN dt.target_type = 'category' THEN c.name
        ELSE 'All Products'
    END AS target_name
FROM discounts d
LEFT JOIN discount_targets dt ON d.id = dt.discount_id
LEFT JOIN products p ON dt.target_type = 'product' AND dt.target_id = p.id
LEFT JOIN product_variants pv ON dt.target_type = 'variant' AND dt.target_id = pv.id
LEFT JOIN categories c ON dt.target_type = 'category' AND dt.target_id = c.id
WHERE d.is_active = 1 AND GETDATE() BETWEEN d.start_date AND ISNULL(d.end_date, GETDATE());
-- Xem đơn hàng và discount được áp dụng
SELECT 
    o.order_code,
    o.customer_name,
    o.subtotal_amount,
    o.discount_amount,
    o.total_amount,
    oi.product_name,
    oi.variant_attributes,
    oi.unit_sale_price,
    oi.discount_amount AS item_discount,
    oi.total_price AS item_total
FROM orders o
JOIN order_items oi ON o.id = oi.order_id

select * from products
update products set is_featured =1 where id =1
select * from categories

select *from discounts