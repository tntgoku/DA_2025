package backend.main.Request;

import java.math.BigDecimal;

    public class OrderItemRequest {
        private Integer id;
        private Integer orderId;
        private Integer inventoryId;
        private Integer variantId;
        private String productName;
        private String variantAttributes;
        private String sku;
        private BigDecimal unitSalePrice;
        private BigDecimal unitCostPrice;
        private Integer quantity;
        private BigDecimal totalPrice;
        private Integer warrantyMonths;

    public OrderItemRequest() {
    }

    @Override
    public String toString() {
        return "OrderItemRequest [id=" + id + ", orderId=" + orderId + ", inventoryId=" + inventoryId + ", variantId="
                + variantId + ", productName=" + productName + ", variantAttributes=" + variantAttributes + ", sku="
                + sku + ", unitSalePrice=" + unitSalePrice + ", unitCostPrice=" + unitCostPrice + ", quantity="
                + quantity + ", totalPrice=" + totalPrice + ", warrantyMonths=" + warrantyMonths + "]";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVariantAttributes() {
        return variantAttributes;
    }

    public void setVariantAttributes(String variantAttributes) {
        this.variantAttributes = variantAttributes;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getUnitSalePrice() {
        return unitSalePrice;
    }

    public void setUnitSalePrice(BigDecimal unitSalePrice) {
        this.unitSalePrice = unitSalePrice;
    }

    public BigDecimal getUnitCostPrice() {
        return unitCostPrice;
    }

    public void setUnitCostPrice(BigDecimal unitCostPrice) {
        this.unitCostPrice = unitCostPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(Integer warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }


}
