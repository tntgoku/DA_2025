package backend.main.Request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryRequest {
    private Integer id;
    private Integer productVariant;
    private Integer condition;
    private Integer source;
    private String imei;
    private String serialNumber;
    private BigDecimal costPrice;
    private BigDecimal salePrice;
    private BigDecimal listPrice;
    private String status;
    private String position;
    private Integer stock;
    private LocalDateTime importDate;
    private LocalDateTime soldDate;
    private Integer warrantyMonths;
    private String deviceConditionNotes;
    private String previousOwnerInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(Integer productVariant) {
        this.productVariant = productVariant;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public LocalDateTime getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDateTime importDate) {
        this.importDate = importDate;
    }

    public LocalDateTime getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(LocalDateTime soldDate) {
        this.soldDate = soldDate;
    }

    public Integer getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(Integer warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public String getDeviceConditionNotes() {
        return deviceConditionNotes;
    }

    public void setDeviceConditionNotes(String deviceConditionNotes) {
        this.deviceConditionNotes = deviceConditionNotes;
    }

    public String getPreviousOwnerInfo() {
        return previousOwnerInfo;
    }

    public void setPreviousOwnerInfo(String previousOwnerInfo) {
        this.previousOwnerInfo = previousOwnerInfo;
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

    public InventoryRequest() {
    }

    @Override
    public String toString() {
        return "InventoryRequest [id=" + id + ", productVariant=" + productVariant + ", condition=" + condition
                + ", source=" + source + ", imei=" + imei + ", serialNumber=" + serialNumber + ", costPrice="
                + costPrice + ", salePrice=" + salePrice + ", listPrice=" + listPrice + ", status=" + status
                + ", position=" + position + ", stock=" + stock + ", importDate=" + importDate + ", soldDate="
                + soldDate + ", warrantyMonths=" + warrantyMonths + ", deviceConditionNotes=" + deviceConditionNotes
                + ", previousOwnerInfo=" + previousOwnerInfo + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
                + "]";
    }

}
