-- DDL: TẠO CẤU TRÚC BẢNG (ĐÃ SỬA LỖI CÚ PHÁP)
CREATE DATABASE trungghieu
GO
USE trungghieu
GO
-- === DANH MỤC & SẢN PHẨM ===
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
-- === PHIÊN BẢN SẢN PHẨM ===
CREATE TABLE product_variants(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    product_id INT NOT NULL,
    name_variants nvarchar(max),
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

-- === THÔNG SỐ KỸ THUẬT ===
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

-- === TÌNH TRẠNG & NGUỒN HÀNG ===
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

-- === TÀI KHOẢN & KHÁCH HÀNG ===
CREATE TABLE roles(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    role_name NVARCHAR(50) NOT NULL UNIQUE,
    description NVARCHAR(255) -- LỖI CÚ PHÁP ĐÃ ĐƯỢC SỬA
);
select * from inventory_items
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

-- === NGUỒN HÀNG & QUẢN LÝ KHO ===
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
    condition_id INT NULL,
    source_id INT NULL,
    imei NVARCHAR(50) DEFAULT NULL,
    serial_number NVARCHAR(100),
    cost_price DECIMAL(18, 2) NOT NULL,
    sale_price DECIMAL(18, 2) NOT NULL,
    list_price DECIMAL(18, 2),
    status NVARCHAR(50) DEFAULT 'available'
    CHECK(status IN('available', 'sold', 'reserved', 'defective', 'returned')),
	stock INT DEFAULT 0,
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

--alter TABLE INVENTORY_ITEMS
--ADD stock  INT DEFAULT 0

-- === KHUYẾN MÃI & VOUCHER ===
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
    target_type NVARCHAR(50) NOT NULL CHECK(target_type IN('product', 'category', 'variant', 'all')),
    target_id INT NULL,
    FOREIGN KEY(discount_id) REFERENCES discounts(id) ON DELETE CASCADE
);

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

-- === TAG SẢN PHẨM & HIỂN THỊ ===
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

-- === HÌNH ẢNH SẢN PHẨM ===
CREATE TABLE product_images(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    product_variant_id INT NULL,
	product_id int null,
    image_url NVARCHAR(500) NOT NULL,
    image_type NVARCHAR(50) ,
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

-- === ĐƠN HÀNG & BÁN HÀNG ===
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
    inventory_item_id INT NULL,
	variant_id int null,
    product_name NVARCHAR(255) NOT NULL,
    variant_attributes NVARCHAR(500) NOT NULL,
    sku NVARCHAR(100) NOT NULL,
    condition_type NVARCHAR(50) NOT NULL,
    imei NVARCHAR(50), 
    unit_cost_price DECIMAL(18, 2) NOT NULL, -- 
    unit_sale_price DECIMAL(18, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    discount_amount DECIMAL(18, 2) DEFAULT 0,
    total_price DECIMAL(18, 2) NOT NULL,
    warranty_months INT,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY(inventory_item_id) REFERENCES inventory_items(id),
	FOREIGN KEY(variant_id) REFERENCES product_variants(id)
);
-- === TRẢ GÓP ===
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

-- === DỊCH VỤ SỬA CHỮA ===
CREATE TABLE repair_services(
    id INT IDENTITY(1, 1) PRIMARY KEY,
    category_id INT NOT NULL,
    estimated_time INT, -- thời gian ước tính (phút) 
    warranty_days INT, -- bảo hành dịch vụ (ngày) 
    base_price DECIMAL(18, 2) NOT NULL, -- giá dịch vụ 
    is_active BIT DEFAULT 1, -- LỖI CÚ PHÁP ĐÃ ĐƯỢC SỬA
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
    FOREIGN KEY(customer_id) REFERENCES users(id)
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
GO
use trungghieu
go
select *from inventory_items
go
select * from product_variants
go

select distinct p.*,  pv.storage from products p 
left join product_variants pv 
on p.id= pv.product_id

select * from products
go
select * from discounts
go
select * from discount_campaigns
go
select * from discount_targets