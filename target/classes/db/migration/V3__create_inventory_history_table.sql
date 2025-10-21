-- Create inventory_history table
CREATE TABLE IF NOT EXISTS inventory_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    inventory_item_id INT,
    product_variant_id INT,
    action_type ENUM('IMPORT', 'EXPORT', 'ADJUSTMENT', 'TRANSFER', 'RETURN', 'DAMAGE', 'LOSS', 'AUDIT') NOT NULL,
    quantity_change INT NOT NULL,
    quantity_before INT,
    quantity_after INT,
    reason VARCHAR(255),
    notes TEXT,
    reference_id VARCHAR(255),
    reference_type VARCHAR(100),
    unit_cost DECIMAL(15,2),
    total_cost DECIMAL(15,2),
    performed_by VARCHAR(255),
    performed_at DATETIME NOT NULL,
    location VARCHAR(255),
    supplier VARCHAR(255),
    batch_number VARCHAR(255),
    expiry_date DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (inventory_item_id) REFERENCES inventory_items(id) ON DELETE CASCADE,
    FOREIGN KEY (product_variant_id) REFERENCES product_variants(id) ON DELETE CASCADE,
    
    INDEX idx_inventory_item_id (inventory_item_id),
    INDEX idx_product_variant_id (product_variant_id),
    INDEX idx_action_type (action_type),
    INDEX idx_performed_at (performed_at),
    INDEX idx_reference (reference_id, reference_type)
);

-- Insert sample data for testing
INSERT INTO inventory_history (
    inventory_item_id, 
    product_variant_id, 
    action_type, 
    quantity_change, 
    quantity_before, 
    quantity_after, 
    reason, 
    notes, 
    performed_by, 
    performed_at
) VALUES 
(1, 1, 'IMPORT', 50, 0, 50, 'Nhập hàng lần đầu', 'Nhập hàng từ nhà cung cấp ABC', 'admin', NOW()),
(1, 1, 'EXPORT', -5, 50, 45, 'Bán hàng', 'Bán cho khách hàng XYZ', 'admin', NOW()),
(1, 1, 'ADJUSTMENT', 10, 45, 55, 'Điều chỉnh tồn kho', 'Kiểm kê cuối tháng', 'admin', NOW());
