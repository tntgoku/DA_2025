package backend.main.DTO.PromotionDTO;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class VourcherDTO {
    private Integer id;
    private String code;
    private String name;
    private String description;
    private String discountType;
    private BigDecimal value;
    private BigDecimal maxDiscount;
    private BigDecimal minOrderValue;
    private Integer totalUses;
    private Integer maxUses;
    private Integer maxUsesPerUser;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public VourcherDTO(){
        
    }
    public VourcherDTO(Integer id, String code, String name, String description, String discountType, BigDecimal value,
            BigDecimal maxDiscount, BigDecimal minOrderValue, Integer totalUses, Integer maxUses,
            Integer maxUsesPerUser, LocalDateTime startDate, LocalDateTime endDate, Boolean isActive, Integer priority,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.discountType = discountType;
        this.value = value;
        this.maxDiscount = maxDiscount;
        this.minOrderValue = minOrderValue;
        this.totalUses = totalUses;
        this.maxUses = maxUses;
        this.maxUsesPerUser = maxUsesPerUser;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    @Override
    public String toString() {
        return "VourcherDTO [id=" + id + ", code=" + code + ", name=" + name + ", description=" + description
                + ", discountType=" + discountType + ", value=" + value + ", maxDiscount=" + maxDiscount
                + ", minOrderValue=" + minOrderValue + ", totalUses=" + totalUses + ", maxUses=" + maxUses
                + ", maxUsesPerUser=" + maxUsesPerUser + ", startDate=" + startDate + ", endDate=" + endDate
                + ", isActive=" + isActive + ", priority=" + priority + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + "]";
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDiscountType() {
        return discountType;
    }
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }
    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }
    public BigDecimal getMinOrderValue() {
        return minOrderValue;
    }
    public void setMinOrderValue(BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }
    public Integer getTotalUses() {
        return totalUses;
    }
    public void setTotalUses(Integer totalUses) {
        this.totalUses = totalUses;
    }
    public Integer getMaxUses() {
        return maxUses;
    }
    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }
    public Integer getMaxUsesPerUser() {
        return maxUsesPerUser;
    }
    public void setMaxUsesPerUser(Integer maxUsesPerUser) {
        this.maxUsesPerUser = maxUsesPerUser;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
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
    
}
