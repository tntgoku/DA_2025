package backend.main.DTO;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItemDTO {
    private Integer id;
    private Integer orderId;
    private Integer inventoryId;
    private Integer variantId;
    private String productName;
    private String variantAttributes;
    private String sku;
    private BigDecimal unitSalePrice;
    private BigDecimal unitCostPrice;
    private Integer quantity = 1;
    private BigDecimal totalPrice;
    private Integer warrantyMonths;

    public OrderItemDTO() {
    }

    public OrderItemDTO(Integer id, Integer orderId, Integer inventoryId, Integer variantId, String productName,
            String variantAttributes, String sku, BigDecimal unitSalePrice, BigDecimal unitCostPrice, Integer quantity,
            BigDecimal totalPrice, Integer warrantyMonths) {
        this.id = id;
        this.orderId = orderId;
        this.inventoryId = inventoryId;
        this.variantId = variantId;
        this.productName = productName;
        this.variantAttributes = variantAttributes;
        this.sku = sku;
        this.unitSalePrice = unitSalePrice;
        this.unitCostPrice = unitCostPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.warrantyMonths = warrantyMonths;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getInventoryId() {
        return this.inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getVariantId() {
        return this.variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVariantAttributes() {
        return this.variantAttributes;
    }

    public void setVariantAttributes(String variantAttributes) {
        this.variantAttributes = variantAttributes;
    }

    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getUnitSalePrice() {
        return this.unitSalePrice;
    }

    public void setUnitSalePrice(BigDecimal unitSalePrice) {
        this.unitSalePrice = unitSalePrice;
    }

    public BigDecimal getUnitCostPrice() {
        return this.unitCostPrice;
    }

    public void setUnitCostPrice(BigDecimal unitCostPrice) {
        this.unitCostPrice = unitCostPrice;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getWarrantyMonths() {
        return this.warrantyMonths;
    }

    public void setWarrantyMonths(Integer warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public OrderItemDTO id(Integer id) {
        setId(id);
        return this;
    }

    public OrderItemDTO orderId(Integer orderId) {
        setOrderId(orderId);
        return this;
    }

    public OrderItemDTO inventoryId(Integer inventoryId) {
        setInventoryId(inventoryId);
        return this;
    }

    public OrderItemDTO variantId(Integer variantId) {
        setVariantId(variantId);
        return this;
    }

    public OrderItemDTO productName(String productName) {
        setProductName(productName);
        return this;
    }

    public OrderItemDTO variantAttributes(String variantAttributes) {
        setVariantAttributes(variantAttributes);
        return this;
    }

    public OrderItemDTO sku(String sku) {
        setSku(sku);
        return this;
    }

    public OrderItemDTO unitSalePrice(BigDecimal unitSalePrice) {
        setUnitSalePrice(unitSalePrice);
        return this;
    }

    public OrderItemDTO unitCostPrice(BigDecimal unitCostPrice) {
        setUnitCostPrice(unitCostPrice);
        return this;
    }

    public OrderItemDTO quantity(Integer quantity) {
        setQuantity(quantity);
        return this;
    }

    public OrderItemDTO totalPrice(BigDecimal totalPrice) {
        setTotalPrice(totalPrice);
        return this;
    }

    public OrderItemDTO warrantyMonths(Integer warrantyMonths) {
        setWarrantyMonths(warrantyMonths);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderItemDTO)) {
            return false;
        }
        OrderItemDTO orderItemDTO = (OrderItemDTO) o;
        return Objects.equals(id, orderItemDTO.id) && Objects.equals(orderId, orderItemDTO.orderId)
                && Objects.equals(inventoryId, orderItemDTO.inventoryId)
                && Objects.equals(variantId, orderItemDTO.variantId)
                && Objects.equals(productName, orderItemDTO.productName)
                && Objects.equals(variantAttributes, orderItemDTO.variantAttributes)
                && Objects.equals(sku, orderItemDTO.sku) && Objects.equals(unitSalePrice, orderItemDTO.unitSalePrice)
                && Objects.equals(unitCostPrice, orderItemDTO.unitCostPrice)
                && Objects.equals(quantity, orderItemDTO.quantity)
                && Objects.equals(totalPrice, orderItemDTO.totalPrice)
                && Objects.equals(warrantyMonths, orderItemDTO.warrantyMonths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, inventoryId, variantId, productName, variantAttributes, sku, unitSalePrice,
                unitCostPrice, quantity, totalPrice, warrantyMonths);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", orderId='" + getOrderId() + "'" +
                ", inventoryId='" + getInventoryId() + "'" +
                ", variantId='" + getVariantId() + "'" +
                ", productName='" + getProductName() + "'" +
                ", variantAttributes='" + getVariantAttributes() + "'" +
                ", sku='" + getSku() + "'" +
                ", unitSalePrice='" + getUnitSalePrice() + "'" +
                ", unitCostPrice='" + getUnitCostPrice() + "'" +
                ", quantity='" + getQuantity() + "'" +
                ", totalPrice='" + getTotalPrice() + "'" +
                ", warrantyMonths='" + getWarrantyMonths() + "'" +
                "}";
    }

}
