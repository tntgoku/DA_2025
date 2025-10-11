package backend.main.Request;

import java.math.BigDecimal;

public class VariantRequest {
    private Integer variantId;
    private String nameVariants;

    private String sku;

    private String color;

    private String colorCode;

    private String storage;

    private String ram;

    private String regionCode;

    private Boolean isActive;
    private BigDecimal price;
    private BigDecimal sale_price;
    private BigDecimal list_price;
    private BigDecimal discount;
    private Integer warrantly;
    private Integer stock;
    private String status;
    // Getters and Setters

    public String getNameVariants() {
        return nameVariants;
    }

    @Override
    public String toString() {
        return "VariantRequest [variantId=" + variantId + ", nameVariants=" + nameVariants + ", sku=" + sku + ", color="
                + color + ", colorCode=" + colorCode + ", storage=" + storage + ", ram=" + ram + ", regionCode="
                + regionCode + ", isActive=" + isActive + ", price=" + price + ", sale_price=" + sale_price
                + ", list_price=" + list_price + ", discount=" + discount + ", warrantly=" + warrantly + ", stock="
                + stock + ", status=" + status + "]";
    }

    public void setNameVariants(String nameVariants) {
        this.nameVariants = nameVariants;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSale_price() {
        return sale_price;
    }

    public void setSale_price(BigDecimal sale_price) {
        this.sale_price = sale_price;
    }

    public BigDecimal getList_price() {
        return list_price;
    }

    public void setList_price(BigDecimal list_price) {
        this.list_price = list_price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getWarrantly() {
        return warrantly;
    }

    public void setWarrantly(Integer warrantly) {
        this.warrantly = warrantly;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
