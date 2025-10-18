package backend.main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryDTO {
    private Integer id;
    private Integer productId;
    private String productName;
    private Integer variantId;
    private String variantName;
    private String color;
    private String storage;
    private String sku;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Integer currentStock;
    private Integer minStock;
    private Integer maxStock;
    private Integer reorderPoint;
    private String status;
    private String location;
    private String notes;
    private String stockStatus; // "in-stock", "low-stock", "out-of-stock"
    private BigDecimal totalValue;
    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public InventoryDTO() {}

    public InventoryDTO(Integer id, Integer productId, String productName, Integer variantId, 
                       String variantName, String color, String storage, String sku,
                       BigDecimal price, BigDecimal salePrice, Integer currentStock,
                       Integer minStock, Integer maxStock, Integer reorderPoint,
                       String status, String location, String notes) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.variantId = variantId;
        this.variantName = variantName;
        this.color = color;
        this.storage = storage;
        this.sku = sku;
        this.price = price;
        this.salePrice = salePrice;
        this.currentStock = currentStock;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.reorderPoint = reorderPoint;
        this.status = status;
        this.location = location;
        this.notes = notes;
        this.calculateStockStatus();
        this.calculateTotalValue();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        this.calculateTotalValue();
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
        this.calculateStockStatus();
        this.calculateTotalValue();
    }

    public Integer getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
        this.calculateStockStatus();
    }

    public Integer getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(Integer maxStock) {
        this.maxStock = maxStock;
    }

    public Integer getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(Integer reorderPoint) {
        this.reorderPoint = reorderPoint;
        this.calculateStockStatus();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    private void calculateStockStatus() {
        if (currentStock == null || currentStock == 0) {
            this.stockStatus = "out-of-stock";
        } else if (reorderPoint != null && currentStock <= reorderPoint) {
            this.stockStatus = "low-stock";
        } else {
            this.stockStatus = "in-stock";
        }
    }

    private void calculateTotalValue() {
        if (currentStock != null && price != null) {
            this.totalValue = price.multiply(BigDecimal.valueOf(currentStock));
        } else {
            this.totalValue = BigDecimal.ZERO;
        }
    }

    // Utility methods
    public boolean isLowStock() {
        return "low-stock".equals(stockStatus);
    }

    public boolean isOutOfStock() {
        return "out-of-stock".equals(stockStatus);
    }

    public boolean isInStock() {
        return "in-stock".equals(stockStatus);
    }

    public boolean needsReorder() {
        return reorderPoint != null && currentStock != null && currentStock <= reorderPoint;
    }

    @Override
    public String toString() {
        return "InventoryDTO{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", variantId=" + variantId +
                ", variantName='" + variantName + '\'' +
                ", color='" + color + '\'' +
                ", storage='" + storage + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", currentStock=" + currentStock +
                ", stockStatus='" + stockStatus + '\'' +
                ", totalValue=" + totalValue +
                '}';
    }
}
