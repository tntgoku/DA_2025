package backend.main.Request;

import org.springframework.web.multipart.MultipartFile;

public  class ProductImageRequest {
    private String imageUrl;
    private String altImg;
    private Integer productId;
    private Integer variantId;
    private Integer displayOrder;
    private Boolean isPrimary;
    private String imageType;

    @Override
    public String toString() {
        return "ProductImageRequest [imageUrl=" + imageUrl + ", altImg=" + altImg + ", variantId=" + variantId
                + ", displayOrder=" + displayOrder + ", isPrimary=" + isPrimary + ", imageType=" + imageType + "]";
        }
        public ProductImageRequest(){
            
        }
        public ProductImageRequest(String imageUrl, String altImg, Integer variantId,Integer productId, Integer displayOrder,
            Boolean isPrimary, String imageType) {
        this.imageUrl = imageUrl;
        this.altImg = altImg;
        this.variantId = variantId;
        this.productId = productId;
        this.displayOrder = displayOrder;
        this.isPrimary = isPrimary;
        this.imageType = imageType;
    }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    // Getters and Setters
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public String getAltImg() { return altImg; }
    public void setAltImg(String altImg) { this.altImg = altImg; }
    
    public Integer getVariantId() { return variantId; }
    public void setVariantId(Integer variantId) { this.variantId = variantId; }
    
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    
    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
    
    public String getImageType() { return imageType; }
    public void setImageType(String imageType) { this.imageType = imageType; }
}
