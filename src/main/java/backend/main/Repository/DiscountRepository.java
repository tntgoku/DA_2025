package backend.main.Repository;

import backend.main.DTO.PromotionDTO.PromotionDTO;
import backend.main.DTO.PromotionDTO.DiscountCampaignProjection;
import backend.main.Model.Promotion.Discount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository cho Discount entity
 */
@Repository
public interface DiscountRepository extends BaseRepository<Discount, Integer> {

    /**
     * Tìm các discount đang hoạt động
     */
    List<Discount> findByIsActiveTrueAndStartDateBeforeAndEndDateAfter(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Tìm các discount sắp bắt đầu
     */
    List<Discount> findByStartDateAfter(LocalDateTime startDate);

    /**
     * Tìm các discount sắp kết thúc
     */
    List<Discount> findByEndDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Tìm discount theo trạng thái
     */
    List<Discount> findByIsActive(Boolean isActive);

    /**
     * Tìm discount theo loại
     */
    List<Discount> findByDiscountType(backend.main.Model.Promotion.DiscountType discountType);


    @Query(value = """
                SELECT dt.id as discountTargetId,
                       dt.target_type as targetType,
                       d.value,
                       d.is_active as isActive,
                       d.discount_type as discountType,
                       p.id as productId,
                       p.name as productName
                FROM discount_targets dt
                JOIN discounts d ON d.id = dt.discount_id
                JOIN products p ON p.id = dt.target_id
                WHERE dt.target_type = 'product'
            """, nativeQuery = true)
    List<PromotionDTO> getDiscountInventoryNative();

    @Query(value = """
        SELECT 
            a.id as campaignId,
            a.name as campaignName,
            a.description as campaignDescription,
            a.campaign_type as campaignType,
            a.start_date as startDate,
            a.end_date as endDate,
            a.is_active as isActive,
            a.created_at as createdAt,
            a.priority as priority,
            b.value as value,
            b.max_discount as maxDiscount,
            b.min_order_value as minOrderValue,
            dt.target_type as targetType,
            dt.is_included as isIncluded
        FROM discount_campaigns a 
        JOIN discounts b ON a.id = b.campaign_id
        JOIN discount_targets dt ON dt.id = b.id
        """, nativeQuery = true)
List<DiscountCampaignProjection> findAllDiscountCampaign();

}