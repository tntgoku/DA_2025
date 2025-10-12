package backend.main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import backend.main.Model.Promotion.DiscountCampaign;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;

public class VoucherDTO {

    private Integer id;

    private String code;

    private String name;

    private String description;

    private String discountType; // 'percentage' or 'fixed_amount'

    private BigDecimal value;

    private BigDecimal maxDiscount;

    private BigDecimal minOrderValue;

    private Integer totalUses = 0;

    private Integer maxUses;

    private Integer maxUsesPerUser;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean isActive = true;

    // Liên kết tới DiscountCampaign
    private DiscountCampaign campaign;

    private LocalDateTime createdAt = LocalDateTime.now();

    public VoucherDTO() {
    }

    public VoucherDTO(Integer id, String code, String name, String description, String discountType, BigDecimal value,
            BigDecimal maxDiscount, BigDecimal minOrderValue, Integer totalUses, Integer maxUses,
            Integer maxUsesPerUser, LocalDateTime startDate, LocalDateTime endDate, Boolean isActive,
            DiscountCampaign campaign, LocalDateTime createdAt) {
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
        this.campaign = campaign;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscountType() {
        return this.discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getMaxDiscount() {
        return this.maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public BigDecimal getMinOrderValue() {
        return this.minOrderValue;
    }

    public void setMinOrderValue(BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public Integer getTotalUses() {
        return this.totalUses;
    }

    public void setTotalUses(Integer totalUses) {
        this.totalUses = totalUses;
    }

    public Integer getMaxUses() {
        return this.maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public Integer getMaxUsesPerUser() {
        return this.maxUsesPerUser;
    }

    public void setMaxUsesPerUser(Integer maxUsesPerUser) {
        this.maxUsesPerUser = maxUsesPerUser;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean isIsActive() {
        return this.isActive;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public DiscountCampaign getCampaign() {
        return this.campaign;
    }

    public void setCampaign(DiscountCampaign campaign) {
        this.campaign = campaign;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public VoucherDTO id(Integer id) {
        setId(id);
        return this;
    }

    public VoucherDTO code(String code) {
        setCode(code);
        return this;
    }

    public VoucherDTO name(String name) {
        setName(name);
        return this;
    }

    public VoucherDTO description(String description) {
        setDescription(description);
        return this;
    }

    public VoucherDTO discountType(String discountType) {
        setDiscountType(discountType);
        return this;
    }

    public VoucherDTO value(BigDecimal value) {
        setValue(value);
        return this;
    }

    public VoucherDTO maxDiscount(BigDecimal maxDiscount) {
        setMaxDiscount(maxDiscount);
        return this;
    }

    public VoucherDTO minOrderValue(BigDecimal minOrderValue) {
        setMinOrderValue(minOrderValue);
        return this;
    }

    public VoucherDTO totalUses(Integer totalUses) {
        setTotalUses(totalUses);
        return this;
    }

    public VoucherDTO maxUses(Integer maxUses) {
        setMaxUses(maxUses);
        return this;
    }

    public VoucherDTO maxUsesPerUser(Integer maxUsesPerUser) {
        setMaxUsesPerUser(maxUsesPerUser);
        return this;
    }

    public VoucherDTO startDate(LocalDateTime startDate) {
        setStartDate(startDate);
        return this;
    }

    public VoucherDTO endDate(LocalDateTime endDate) {
        setEndDate(endDate);
        return this;
    }

    public VoucherDTO isActive(Boolean isActive) {
        setIsActive(isActive);
        return this;
    }

    public VoucherDTO campaign(DiscountCampaign campaign) {
        setCampaign(campaign);
        return this;
    }

    public VoucherDTO createdAt(LocalDateTime createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof VoucherDTO)) {
            return false;
        }
        VoucherDTO voucherDTO = (VoucherDTO) o;
        return Objects.equals(id, voucherDTO.id) && Objects.equals(code, voucherDTO.code)
                && Objects.equals(name, voucherDTO.name) && Objects.equals(description, voucherDTO.description)
                && Objects.equals(discountType, voucherDTO.discountType) && Objects.equals(value, voucherDTO.value)
                && Objects.equals(maxDiscount, voucherDTO.maxDiscount)
                && Objects.equals(minOrderValue, voucherDTO.minOrderValue)
                && Objects.equals(totalUses, voucherDTO.totalUses) && Objects.equals(maxUses, voucherDTO.maxUses)
                && Objects.equals(maxUsesPerUser, voucherDTO.maxUsesPerUser)
                && Objects.equals(startDate, voucherDTO.startDate) && Objects.equals(endDate, voucherDTO.endDate)
                && Objects.equals(isActive, voucherDTO.isActive) && Objects.equals(campaign, voucherDTO.campaign)
                && Objects.equals(createdAt, voucherDTO.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, description, discountType, value, maxDiscount, minOrderValue, totalUses,
                maxUses, maxUsesPerUser, startDate, endDate, isActive, campaign, createdAt);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", code='" + getCode() + "'" +
                ", name='" + getName() + "'" +
                ", description='" + getDescription() + "'" +
                ", discountType='" + getDiscountType() + "'" +
                ", value='" + getValue() + "'" +
                ", maxDiscount='" + getMaxDiscount() + "'" +
                ", minOrderValue='" + getMinOrderValue() + "'" +
                ", totalUses='" + getTotalUses() + "'" +
                ", maxUses='" + getMaxUses() + "'" +
                ", maxUsesPerUser='" + getMaxUsesPerUser() + "'" +
                ", startDate='" + getStartDate() + "'" +
                ", endDate='" + getEndDate() + "'" +
                ", isActive='" + isIsActive() + "'" +
                ", campaign='" + getCampaign() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                "}";
    }

}
