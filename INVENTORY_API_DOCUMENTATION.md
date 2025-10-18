# 📦 Inventory Management API Documentation

## 🎯 Tổng Quan

API quản lý kho cung cấp đầy đủ các endpoints để quản lý tồn kho sản phẩm, bao gồm CRUD operations, nhập/xuất kho, báo cáo và thống kê.

**Base URL**: `http://localhost:8080/api/inventory`

## 📋 API Endpoints

### 🔧 Basic CRUD Operations

#### 1. Lấy tất cả inventory
```http
GET /api/inventory
```

**Response:**
```json
{
  "status": 200,
  "message": "Thành công",
  "count": 0,
  "data": [
    {
      "id": 1,
      "productVariant": {
        "id": 1,
        "product": {
          "id": 1,
          "name": "iPhone 15 Pro"
        },
        "sku": "IP15P-128-TN",
        "salePrice": 27990000
      },
      "stock": 50,
      "status": "active",
      "position": "Kho A, Kệ 1",
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    }
  ]
}
```

#### 2. Lấy inventory theo ID
```http
GET /api/inventory/{id}
```

#### 3. Tạo inventory mới
```http
POST /api/inventory
Content-Type: application/json

{
  "productVariant": 1,
  "condition": 1,
  "source": 1,
  "imei": "123456789012345",
  "serialNumber": "SN123456",
  "status": "active",
  "position": "Kho A, Kệ 1",
  "stock": 50,
  "importDate": "2024-01-01T10:00:00",
  "warrantyMonths": 12,
  "deviceConditionNotes": "Mới 100%",
  "previousOwnerInfo": "Không có"
}
```

#### 4. Cập nhật inventory
```http
PUT /api/inventory/{id}
Content-Type: application/json

{
  "stock": 45,
  "status": "active",
  "position": "Kho A, Kệ 2",
  "notes": "Đã bán 5 sản phẩm"
}
```

#### 5. Xóa inventory
```http
DELETE /api/inventory/{id}
```

### 📦 Stock Management

#### 6. Cập nhật số lượng tồn kho
```http
PATCH /api/inventory/{id}/stock
Content-Type: application/json

{
  "stock": 100,
  "reason": "Điều chỉnh tồn kho",
  "notes": "Kiểm kê cuối tháng"
}
```

#### 7. Nhập kho
```http
POST /api/inventory/{id}/import
Content-Type: application/json

{
  "quantity": 20,
  "reason": "purchase",
  "notes": "Nhập hàng từ nhà cung cấp ABC",
  "unitCost": 25000000,
  "supplier": "Nhà cung cấp ABC",
  "batchNumber": "BATCH001"
}
```

#### 8. Xuất kho
```http
POST /api/inventory/{id}/export
Content-Type: application/json

{
  "quantity": 5,
  "reason": "sale",
  "notes": "Bán cho khách hàng XYZ",
  "referenceId": "ORDER123",
  "referenceType": "order"
}
```

### 📊 Reports and Analytics

#### 9. Báo cáo tổng hợp
```http
GET /api/inventory/report?startDate=2024-01-01&endDate=2024-12-31
```

**Response:**
```json
{
  "status": 200,
  "message": "Báo cáo tồn kho",
  "count": 0,
  "data": {
    "totalProducts": 150,
    "totalStock": 2500,
    "lowStockCount": 15,
    "outOfStockCount": 5,
    "totalValue": 125000000000
  }
}
```

#### 10. Sản phẩm sắp hết hàng
```http
GET /api/inventory/low-stock?threshold=10
```

#### 11. Sản phẩm hết hàng
```http
GET /api/inventory/out-of-stock
```

#### 12. Thống kê tổng quan
```http
GET /api/inventory/stats
```

### 📈 History and Tracking

#### 13. Lịch sử tồn kho theo ID
```http
GET /api/inventory/{id}/history
```

**Response:**
```json
{
  "status": 200,
  "message": "Lịch sử tồn kho",
  "count": 0,
  "data": [
    {
      "id": 1,
      "actionType": "IMPORT",
      "quantityChange": 20,
      "quantityBefore": 30,
      "quantityAfter": 50,
      "reason": "Nhập hàng",
      "notes": "Nhập từ nhà cung cấp",
      "performedBy": "admin",
      "performedAt": "2024-01-15T14:30:00"
    }
  ]
}
```

#### 14. Lịch sử tổng hợp
```http
GET /api/inventory/history?startDate=2024-01-01&endDate=2024-01-31
```

### 🔍 Search and Filter

#### 15. Tìm kiếm inventory
```http
GET /api/inventory/search?query=iPhone&status=active&stockStatus=in-stock&minStock=10&maxStock=100
```

**Parameters:**
- `query`: Tìm kiếm theo tên sản phẩm, SKU
- `status`: Trạng thái (active, inactive, discontinued)
- `stockStatus`: Tình trạng tồn kho (in-stock, low-stock, out-of-stock)
- `minStock`: Số lượng tối thiểu
- `maxStock`: Số lượng tối đa

#### 16. Tìm theo biến thể
```http
GET /api/inventory/by-variant/{variantId}
```

#### 17. Tìm theo sản phẩm
```http
GET /api/inventory/by-product/{productId}
```

### 🔄 Bulk Operations

#### 18. Nhập kho hàng loạt
```http
POST /api/inventory/bulk-import
Content-Type: application/json

[
  {
    "inventoryId": 1,
    "quantity": 10,
    "reason": "purchase",
    "notes": "Nhập hàng loạt"
  },
  {
    "inventoryId": 2,
    "quantity": 15,
    "reason": "purchase",
    "notes": "Nhập hàng loạt"
  }
]
```

#### 19. Xuất kho hàng loạt
```http
POST /api/inventory/bulk-export
Content-Type: application/json

[
  {
    "inventoryId": 1,
    "quantity": 5,
    "reason": "sale",
    "notes": "Xuất hàng loạt"
  }
]
```

#### 20. Cập nhật hàng loạt
```http
POST /api/inventory/bulk-update
Content-Type: application/json

[
  {
    "id": 1,
    "stock": 100,
    "status": "active"
  },
  {
    "id": 2,
    "stock": 50,
    "status": "active"
  }
]
```

### ✅ Validation and Checks

#### 21. Kiểm tra inventory
```http
GET /api/inventory/{id}/validate
```

#### 22. Kiểm tra thao tác tồn kho
```http
POST /api/inventory/validate-stock
Content-Type: application/json

{
  "inventoryId": 1,
  "actionType": "export",
  "quantity": 10
}
```

### 📤 Export and Import

#### 23. Xuất dữ liệu
```http
GET /api/inventory/export?format=excel
```

#### 24. Nhập dữ liệu
```http
POST /api/inventory/import
Content-Type: application/json

{
  "format": "excel",
  "data": [...]
}
```

## 🎯 Response Format

Tất cả API responses đều tuân theo format chuẩn:

```json
{
  "status": 200,           // HTTP status code
  "message": "Thành công", // Message mô tả
  "count": 0,              // Số lượng records (nếu có)
  "data": {}               // Dữ liệu trả về
}
```

## 📊 Status Codes

- `200` - Thành công
- `201` - Tạo mới thành công
- `400` - Bad Request (dữ liệu không hợp lệ)
- `404` - Không tìm thấy
- `500` - Lỗi server
- `501` - Chưa được triển khai

## 🔐 Authentication

Tất cả endpoints đều yêu cầu authentication (JWT token):

```http
Authorization: Bearer <your-jwt-token>
```

## 📝 Error Handling

### Validation Errors
```json
{
  "status": 400,
  "message": "Dữ liệu không hợp lệ",
  "count": 0,
  "data": {
    "errors": [
      {
        "field": "quantity",
        "message": "Số lượng phải lớn hơn 0"
      }
    ]
  }
}
```

### Not Found Error
```json
{
  "status": 404,
  "message": "Không tìm thấy tồn kho với ID: 999",
  "count": 1,
  "data": null
}
```

## 🚀 Usage Examples

### 1. Tạo inventory mới
```javascript
const createInventory = async (inventoryData) => {
  const response = await fetch('/api/inventory', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(inventoryData)
  });
  
  const result = await response.json();
  return result;
};
```

### 2. Nhập kho
```javascript
const importStock = async (inventoryId, quantity, reason) => {
  const response = await fetch(`/api/inventory/${inventoryId}/import`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      quantity: quantity,
      reason: reason,
      notes: 'Nhập kho từ nhà cung cấp'
    })
  });
  
  const result = await response.json();
  return result;
};
```

### 3. Tìm kiếm với filter
```javascript
const searchInventory = async (query, filters) => {
  const params = new URLSearchParams();
  if (query) params.append('query', query);
  if (filters.status) params.append('status', filters.status);
  if (filters.stockStatus) params.append('stockStatus', filters.stockStatus);
  
  const response = await fetch(`/api/inventory/search?${params}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  const result = await response.json();
  return result;
};
```

## 📈 Performance Tips

1. **Pagination**: Sử dụng `page` và `size` parameters cho large datasets
2. **Caching**: Cache frequently accessed data
3. **Indexing**: Đảm bảo database có proper indexes
4. **Batch Operations**: Sử dụng bulk operations cho multiple updates

## 🔮 Future Enhancements

- [ ] Real-time notifications
- [ ] Advanced analytics
- [ ] Barcode integration
- [ ] Multi-location support
- [ ] Automated reorder points
- [ ] Integration with accounting systems

---

**API đã sẵn sàng để sử dụng! 🎉**
