package backend.main.DTO.PromotionDTO;

import java.util.List;

import backend.main.DTO.Product.ProductDTO;

public class CampaignDTO {
    private Long campaignId;
    private String campaignName;
    private String campaignDescription;
    private String campaignType;
    private java.time.LocalDateTime startDate;
    private java.time.LocalDateTime endDate;
    private Boolean isActive;
    private java.time.LocalDateTime createdAt;
    private Integer priority;
    private java.math.BigDecimal value;
    private java.math.BigDecimal maxDiscount;
    private java.math.BigDecimal minOrderValue;
    private String targetType;
    private Boolean isIncluded;
    // List<ProductDTO> products;
    public CampaignDTO() {
    }
    @Override
    public String toString() {
        return "CampaignDTO [campaignId=" + campaignId + ", campaignName=" + campaignName + ", campaignDescription="
                + campaignDescription + ", campaignType=" + campaignType + ", startDate=" + startDate + ", endDate="
                + endDate + ", isActive=" + isActive + ", createdAt=" + createdAt + ", priority=" + priority
                + ", value=" + value + ", maxDiscount=" + maxDiscount + ", minOrderValue=" + minOrderValue
                + ", targetType=" + targetType + ", isIncluded=" + isIncluded + "]";
    }

    public CampaignDTO(Long campaignId, String campaignName, String campaignDescription, String campaignType, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, Boolean isActive, java.time.LocalDateTime createdAt, Integer priority, java.math.BigDecimal value, java.math.BigDecimal maxDiscount, java.math.BigDecimal minOrderValue, String targetType, Boolean isIncluded) {

        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.campaignDescription = campaignDescription;
        this.campaignType = campaignType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.priority = priority;
        this.value = value;
        this.maxDiscount = maxDiscount;
        this.minOrderValue = minOrderValue;
        this.targetType = targetType;
        this.isIncluded = isIncluded;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignDescription() {
        return campaignDescription;
    }

    public void setCampaignDescription(String campaignDescription) {
        this.campaignDescription = campaignDescription;
    }

    public String getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    public java.time.LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(java.time.LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public java.time.LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(java.time.LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public java.math.BigDecimal getValue() {
        return value;
    }

    public void setValue(java.math.BigDecimal value) {
        this.value = value;
    }

    public java.math.BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(java.math.BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public java.math.BigDecimal getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(java.math.BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Boolean getIsIncluded() {
        return isIncluded;
    }

    public void setIsIncluded(Boolean isIncluded) {
        this.isIncluded = isIncluded;
    }


}
