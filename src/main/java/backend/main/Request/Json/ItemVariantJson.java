package backend.main.Request.Json;

import java.math.BigDecimal;

public class ItemVariantJson {
    private Integer variantId;
    private Integer productId;
    private String sku;
    private BigDecimal price;
    private Integer stock;
    private String color;
    private String storage;
    private BigDecimal costPrice;
    private Integer stockQuantity;
    private Integer lowStockThreshold;
    private Integer warrantyPeriod;
    private Boolean isActive;
    private Boolean isFromTradeIn;
    private Boolean isFeatured;
    private Integer originalOwnerId;
    private Integer quantity_cart;
    private String region;
    private String slug;

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(Integer lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public Integer getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(Integer warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsFromTradeIn() {
        return isFromTradeIn;
    }

    public void setIsFromTradeIn(Boolean isFromTradeIn) {
        this.isFromTradeIn = isFromTradeIn;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Integer getOriginalOwnerId() {
        return originalOwnerId;
    }

    public void setOriginalOwnerId(Integer originalOwnerId) {
        this.originalOwnerId = originalOwnerId;
    }

    public Integer getQuantity_cart() {
        return quantity_cart;
    }

    public void setQuantity_cart(Integer quantity_cart) {
        this.quantity_cart = quantity_cart;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}


