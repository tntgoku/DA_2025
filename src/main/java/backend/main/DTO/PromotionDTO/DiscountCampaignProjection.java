package backend.main.DTO.PromotionDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO cho kết quả query findAllDiscountCampaign
 * Match với query: SELECT a.id,a.name,a.description,a.campaign_type,a.start_date,a.end_date,a.is_active,a.created_at,a.priority,
 * b.value,b.max_discount,b.min_order_value,dt.target_type,dt.is_included 
 * FROM discount_campaigns a JOIN discounts b ON a.id=b.campaign_id JOIN discount_targets dt ON dt.id= b.id
 */
public interface DiscountCampaignProjection {

    
        Long getCampaignId();
        String getCampaignName();
        String getCampaignDescription();
        String getCampaignType();
        java.time.LocalDateTime getStartDate();
        java.time.LocalDateTime getEndDate();
        Boolean getIsActive();
        java.time.LocalDateTime getCreatedAt();
        Integer getPriority();
        java.math.BigDecimal getValue();
        java.math.BigDecimal getMaxDiscount();
        java.math.BigDecimal getMinOrderValue();
        String getTargetType();
        Boolean getIsIncluded();
    
}