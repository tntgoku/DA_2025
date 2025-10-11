package backend.main.Request.Json;

class VariantStorageJson {
    private Integer variantId;
    private Integer productId;
    private String nameVariants;
    private String sku;
    private String color;
    private String colorCode;
    private String storage;
    private String ram;
    private String regionCode;
    private Boolean isActive;
    private Double price;
    private Double sale_price;
    private Double list_price;
    private Double discount;
    private Integer warrantly;
    private Integer stock;
    private String status;

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

    public String getNameVariants() {
        return nameVariants;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSale_price() {
        return sale_price;
    }

    public void setSale_price(Double sale_price) {
        this.sale_price = sale_price;
    }

    public Double getList_price() {
        return list_price;
    }

    public void setList_price(Double list_price) {
        this.list_price = list_price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
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

    @Override
    public String toString() {
        return "VariantStorageJson [variantId=" + variantId + ", productId=" + productId + ", nameVariants="
                + nameVariants + ", sku=" + sku + ", color=" + color + ", colorCode=" + colorCode + ", storage="
                + storage + ", ram=" + ram + ", regionCode=" + regionCode + ", isActive=" + isActive + ", price="
                + price + ", sale_price=" + sale_price + ", list_price=" + list_price + ", discount=" + discount
                + ", warrantly=" + warrantly + ", stock=" + stock + ", status=" + status + "]";
    }
}
