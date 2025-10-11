USE trungghieu;
GO

-- 1️⃣ Xóa toàn bộ FOREIGN KEY constraints
DECLARE @sql NVARCHAR(MAX) = N'';

SELECT @sql += N'ALTER TABLE ' + QUOTENAME(s.name) + '.' + QUOTENAME(t.name) +
               ' DROP CONSTRAINT ' + QUOTENAME(fk.name) + ';' + CHAR(13)
FROM sys.foreign_keys AS fk
INNER JOIN sys.tables AS t ON fk.parent_object_id = t.object_id
INNER JOIN sys.schemas AS s ON t.schema_id = s.schema_id;

EXEC sp_executesql @sql;
PRINT '✅ Đã xóa toàn bộ FOREIGN KEY constraints';
GO

-- 2️⃣ Sinh lệnh DROP TABLE cho toàn bộ bảng
DECLARE @drop NVARCHAR(MAX) = N'';

SELECT @drop += N'DROP TABLE ' + QUOTENAME(s.name) + '.' + QUOTENAME(t.name) + ';' + CHAR(13)
FROM sys.tables AS t
INNER JOIN sys.schemas AS s ON t.schema_id = s.schema_id;

EXEC sp_executesql @drop;
PRINT '✅ Đã xóa toàn bộ bảng';
GO



---DATA TEST
-- === ROLES ===
INSERT INTO roles (role_name, description) VALUES
('Admin', N'Quản trị viên tối cao'), -- id = 1
('Staff', N'Nhân viên bán hàng/kho hàng'), -- id = 2
('Customer', N'Khách hàng thông thường'); -- id = 3
GO

-- === ACCOUNTS & USERS === (Mật khẩu: hash_...)
-- Admin
INSERT INTO accounts (username, password_hash, role_id) VALUES ('admin', 'hash_admin', 1); -- id = 1
INSERT INTO users (account_id, full_name, phone, email) VALUES (1, N'Nguyễn Văn A (Admin)', '0901111111', 'admin@shop.com'); -- id = 1

-- Staff (Created by for Orders/Repairs)
INSERT INTO accounts (username, password_hash, role_id) VALUES ('staff1', 'hash_staff', 2); -- id = 2
INSERT INTO users (account_id, full_name, phone, email) VALUES (2, N'Trần Thị B (Staff)', '0902222222', 'staff@shop.com'); -- id = 2

-- Registered Customer (Customer_id for Orders)
INSERT INTO accounts (username, password_hash, role_id) VALUES ('customer1', 'hash_customer1', 3); -- id = 3
INSERT INTO users (account_id, full_name, phone, email) VALUES (3, N'Lê Văn C (Khách hàng ĐK)', '0903333333', 'customer1@mail.com'); -- id = 3

-- Guest Customer (Trade-in & Orders)
INSERT INTO users (account_id, full_name, phone, email) VALUES (NULL, N'Phạm Thị D (Khách hàng Vãng lai)', '0904444444', 'guest@mail.com'); -- id = 4
GO

-- === SUPPLIERS (Nhà cung cấp) ===
INSERT INTO suppliers (name, supplier_type) VALUES
(N'Công ty TNHH Apple VN', 'OFFICIAL'), -- id = 1
(N'Công ty Phụ kiện ABC', 'DISTRIBUTOR'), -- id = 2
(N'Công ty TNHH SamSung VN', 'DISTRIBUTOR') -- id = 3
GO

-- === PRODUCT CONDITIONS (Tình trạng sản phẩm) ===
INSERT INTO product_conditions (condition_name, description, discount_rate) VALUES
(N'Mới 100% (New Sealed)', N'Sản phẩm mới nguyên seal.', 0.00), -- id = 1
(N'Likenew 99%', N'Máy đã qua sử dụng, đẹp như mới.', 5.00), -- id = 2 (Giảm 5%)
(N'Cũ 95%', N'Máy đã qua sử dụng, có trầy xước.', 10.00), -- id = 3 (Giảm 10%)
(N'Hàng trưng bày', N'Máy đã kích hoạt.', 3.00); -- id = 4
GO

-- === CATEGORIES ===
INSERT INTO categories (name, slug) VALUES
(N'Điện thoại di động', 'dien-thoai'), -- id = 1
(N'Điện thoại di động cũ', 'dien-thoai-cu'), -- id = 2
(N'Phụ kiện', 'phu-kien'), -- id = 3
(N'Ốp lưng', 'op-lung'), -- id = 4
(N'Dịch vụ sửa chữa', 'dich-vu'); -- id = 5
UPDATE categories SET parent_id = 3 WHERE id = 4;

-- === PRODUCTS ===
-- 1. Điện thoại Mới: iPhone 15 Pro (id = 1)
INSERT INTO products (category_id, product_type, name, slug, brand) VALUES
(1, 'physical', N'iPhone 15 Pro', 'iphone-15-pro', 'Apple'); 
-- iPhone 15 Pro Max
INSERT INTO products (category_id, product_type, name, slug, brand) VALUES
(1, 'physical', N'iPhone 15 Pro Max', 'iphone-15-pro-max', 'Apple');

-- iPhone 15
INSERT INTO products (category_id, product_type, name, slug, brand) VALUES
(1, 'physical', N'iPhone 15', 'iphone-15', 'Apple');

INSERT INTO products (category_id, product_type, name, slug, brand) VALUES
-- iPhone 16 series
(1, 'physical', N'iPhone 16', 'iphone-16', 'Apple'),
(1, 'physical', N'iPhone 16 Pro', 'iphone-16-pro', 'Apple'),
(1, 'physical', N'iPhone 16 Pro Max', 'iphone-16-pro-max', 'Apple');

-- 2. Điện thoại Cũ/Used: Samsung S22 Ultra (id = 2)
INSERT INTO products (category_id, product_type, name, slug, brand) VALUES
(1, 'physical', N'Samsung Galaxy S22 Ultra', 'samsung-s22-ultra', 'Samsung'); 
-- 3. Phụ kiện: Tai nghe (id = 3)
INSERT INTO products (category_id, product_type, name, slug, brand) VALUES
(3, 'physical', N'Tai nghe Air Pro V2', 'air-pro-v2', 'Apple'); 
-- === PRODUCT VARIANTS ===
-- iPhone 15 Pro (id=1)
INSERT INTO product_variants (product_id, name_variants, sku, color, storage, ram) VALUES
(1, N'Titan Đen 256GB', 'IP15P-256-BLK', N'Titan Đen', '256GB', '8GB'), -- id = 1
(1, N'Titan Trắng 512GB', 'IP15P-512-WHT', N'Titan Trắng', '512GB', '8GB'); -- id = 2
-- Samsung S22 Ultra (id=2)
INSERT INTO product_variants (product_id, name_variants, sku, color, storage, ram) VALUES
(2, N'Đen 512GB', 'S22U-512-BLK', N'Đen', '512GB', '12GB'); -- id = 3
-- Tai nghe (id=3)
INSERT INTO product_variants (product_id, name_variants, sku, color, storage, ram) VALUES
(3, N'Màu Trắng', 'APV2-WHT', N'Trắng', 'N/A', 'N/A'); -- id = 4

-- === PRODUCT TAG MAP (Sử dụng cho hiển thị Hot/Mới) ===
INSERT INTO product_tags (tag_name, display_name) VALUES ('new', N'Mới về'); -- id = 1
INSERT INTO product_tag_map (product_id, tag_id) VALUES
(1, 1); -- iPhone 15 Pro là hàng mới

-- === REPAIR SERVICES (Dịch vụ) ===
INSERT INTO repair_services (category_id, estimated_time, warranty_days, base_price) VALUES
(5, 60, 90, 3500000.00), -- id = 1: Thay màn hình cao cấp
(5, 30, 30, 800000.00), -- id = 2: Thay pin chính hãng
(5, 15, 7, 150000.00); -- id = 3: Vệ sinh, tối ưu hệ thống
GO
-- === PRODUCT SOURCES (Nguồn hàng) ===
-- Nhập hàng Mới từ nhà cung cấp chính thức
INSERT INTO product_sources (source_type, supplier_id, reference_number, total_amount) VALUES
('SUPPLIER', 1, 'PO-APL-001', 50000000.00), -- id = 1 (iPhone 15 Pro)
('SUPPLIER', 2, 'PO-ACC-002', 1000000.00); -- id = 2 (Tai nghe)

-- Nhập hàng Cũ từ khách hàng trade-in
INSERT INTO product_sources (source_type, customer_id, reference_number, total_amount) VALUES
('CUSTOMER_TRADE_IN', 4, 'TI-CUS-003', 10000000.00); -- id = 3 (Samsung S22 Ultra)
GO

-- === INVENTORY ITEMS (Từng đơn vị trong kho) ===
-- 1. iPhone 15 Pro MỚI (Variant_id=1, Condition_id=1)
INSERT INTO inventory_items (product_variant_id, condition_id, source_id, imei, cost_price, sale_price, list_price, warranty_months) VALUES
(1, 1, 1, '35123400000001', 22000000.00, 24990000.00, 24990000.00, 12); -- id = 1 (Mới 100%, sẵn có)

-- 2. Samsung S22 Ultra CŨ (Variant_id=3)
INSERT INTO inventory_items (product_variant_id, condition_id, source_id, imei, cost_price, sale_price, list_price, warranty_months, device_condition_notes) VALUES
(3, 2, 3, '35987600000002', 12000000.00, 15990000.00, 16831578.95, 6, N'Máy đẹp 99%, pin 90%'); -- id = 2 (Likenew 99%)
INSERT INTO inventory_items (product_variant_id, condition_id, source_id, imei, cost_price, sale_price, list_price, warranty_months, status) VALUES
(3, 3, 3, '35987600000003', 10000000.00, 12990000.00, 14433333.33, 3, 'reserved'); -- id = 3 (Cũ 95%, đang được đặt)

-- 3. Tai nghe (Variant_id=4, Phụ kiện, không cần IMEI)
INSERT INTO inventory_items (product_variant_id, condition_id, source_id, serial_number, cost_price, sale_price, list_price, warranty_months) VALUES
(4, 1, 2, 'SN-APV2-001', 500000.00, 1290000.00, 1290000.00, 3); -- id = 4
INSERT INTO inventory_items (product_variant_id, condition_id, source_id, serial_number, cost_price, sale_price, list_price, warranty_months) VALUES
(1, 1, 2, 'SN-APV2-001', 11500000.00, 12000000.00, 12300000.00, 3);  -- id = 1
INSERT INTO inventory_items (product_variant_id, condition_id, source_id, serial_number, cost_price, sale_price, list_price, warranty_months) VALUES
(2, 1, 2, 'SN-APV2-001', 11500000.00, 12000000.00, 12300000.00, 3);  -- id = 2
INSERT INTO inventory_items (product_variant_id, condition_id, source_id, serial_number, cost_price, sale_price, list_price, warranty_months) VALUES
(3, 2, 3, 'SS-APV2-001', 11500000.00, 12000000.00, 12300000.00, 3);  -- id = 3
GO

INSERT INTO product_images (product_id, product_variant_id, image_url, image_type, display_order, alt_img, is_primary)
VALUES
-- iPhone 15 Pro - Titan Đen
(1, null, N'https://example.com/images/iphone15pro-titan-black-front.jpg', N'gallery', 1, N'Ảnh chính iPhone 15 Pro Titan Đen', 1),
(1, 1, N'https://example.com/images/iphone15pro-titan-black-back.jpg', N'gallery', 2, N'Ảnh phụ iPhone 15 Pro Titan Đen', 0),

-- iPhone 15 Pro - Titan Trắng
(1, 2, N'https://example.com/images/iphone15pro-titan-white-front.jpg', N'gallery', 1, N'Ảnh chính iPhone 15 Pro Titan Trắng', 1),

-- Samsung Galaxy S22 Ultra - Đen
(2, 3, N'https://example.com/images/s22ultra-black-front.jpg', N'gallery', 1, N'Ảnh chính Galaxy S22 Ultra Đen', 1),

-- Air Pro V2 - Trắng
(3, 4, N'https://example.com/images/airprov2-white.jpg', N'gallery', 1, N'Ảnh chính Tai nghe Air Pro V2 Trắng', 1);
GO

select * from categories;
select * from products;
select * from product_variants;
select * from product_conditions;
select * from accounts;
select * from inventory_items;
update products set category_id =2 where id =2
delete from inventory_items where id=6
select * from products where slug ='iphone-16-pro' or slug= 'iphone-16-pro-max' or slug ='iphone-16'
select * from product_images
delete from product_variants where id >=6 
-- Lấy product_id của iPhone 16 Pro
DECLARE @productId INT;
SELECT @productId = id FROM products WHERE slug ='iphone-16-pro' or slug= 'iphone-16-pro-max' or slug ='iphone-16';

-- Danh sách dung lượng
DECLARE @storages TABLE (storage NVARCHAR(50));
INSERT INTO @storages VALUES ('128GB'), ('256GB'), ('512GB');

-- Danh sách màu sắc
DECLARE @colors TABLE (color NVARCHAR(50));
INSERT INTO @colors VALUES (N'Titan Đen'), (N'Titan-Trắng'), (N'Titan Sa mạc'),(N'Titan Tự nhiên');

-- Danh sách RAM
DECLARE @rams TABLE (ram NVARCHAR(50));
INSERT INTO @rams VALUES ('8GB');

-- Tạo biến thể kết hợp storage + color + ram
DECLARE @storage NVARCHAR(50), @color NVARCHAR(50), @ram NVARCHAR(50);
DECLARE @name NVARCHAR(255), @sku NVARCHAR(100);

DECLARE storage_cursor CURSOR FOR SELECT storage FROM @storages;
OPEN storage_cursor;
FETCH NEXT FROM storage_cursor INTO @storage;

WHILE @@FETCH_STATUS = 0
BEGIN
    DECLARE color_cursor CURSOR FOR SELECT color FROM @colors;
    OPEN color_cursor;
    FETCH NEXT FROM color_cursor INTO @color;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        DECLARE ram_cursor CURSOR FOR SELECT ram FROM @rams;
        OPEN ram_cursor;
        FETCH NEXT FROM ram_cursor INTO @ram;

        WHILE @@FETCH_STATUS = 0
        BEGIN
            SET @name = N'iPhone 16 Pro ' + @storage + ' ' ;
            SET @sku = 'iphone-16-pro-' + LOWER(REPLACE(@storage,'GB','')) + '-' + LOWER(@color);

            INSERT INTO product_variants (product_id, name_variants, sku, color, storage, ram)
            VALUES (@productId, @name, @sku, @color, @storage, @ram);

            FETCH NEXT FROM ram_cursor INTO @ram;
        END

        CLOSE ram_cursor;
        DEALLOCATE ram_cursor;

        FETCH NEXT FROM color_cursor INTO @color;
    END

    CLOSE color_cursor;
    DEALLOCATE color_cursor;

    FETCH NEXT FROM storage_cursor INTO @storage;
END

CLOSE storage_cursor;
DEALLOCATE storage_cursor;

INSERT INTO inventory_items
(product_variant_id, condition_id, source_id, imei, cost_price, sale_price, list_price, warranty_months, stock)
SELECT 
    id,      -- product_variant_id
    1,       -- condition_id
    1,       -- source_id
    NULL,    -- imei
    22000000.00, -- cost_price
    24990000.00, -- sale_price
    24990000.00, -- list_price
    3,      -- warranty_months
    10       -- stock mặc định
FROM product_variants
WHERE id >= 29;
select * from products where slug=  'iphone-16-pro';
SELECT distinct p.id, pv.color,inven.sale_price,inven.stock
FROM products p
LEFT JOIN product_variants pv ON p.id = pv.product_id
LEFT JOIN inventory_items inven on pv.id= inven.product_variant_id
WHERE p.slug = 'iphone-16-pro';

update product_variants set region_code =N'VN/A' where id >=29 and id <=31
select * from products
select *from product_images where product_id=7
select * from discount_targets
select a.slug,a.is_active,a.is_hot,a.category_id,b.product_id,b.id as variantid,b.sku,b.storage,b.color,b.region_code,c.cost_price,c.sale_price,c.list_price,c.status,c.stock,d.discount_id ,d.target_id,e.value from products a 
left join product_variants b on a.id=b.product_id
left join inventory_items c on c.product_variant_id=b.id
join discount_targets d on d.target_id=b.id
join discounts e on e.id= d.discount_id
where d.target_type = N'variant' 
select * from discounts
go
select * from discount_targets
go
select * from discount_campaigns
update discount_targets set discount_id =1 where discount_id=2
insert into product_variants (color,color_code,created_at,is_active,name_variants,product_id,ram,region_code,sku,storage,updated_at)
values (N'Titan Gray',null,getdate(),1,N'test222',15,N'8GB',N'VN/A',N'test2',N'256GB',getdate())

UPDATE product_variants
SET 
    color = N'Titan Gray222',
    color_code = NULL,
    updated_at = GETDATE(),
    is_active = 1,
    name_variants = N'test222',
    product_id = 15,
    ram = N'8GB',
    region_code = N'VN/A',
    sku = N'test2',
    storage = N'256GB'
WHERE id = 47; -- hoặc WHERE sku = N'test2'

