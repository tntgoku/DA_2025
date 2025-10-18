package backend.main.Repository;

import backend.main.Model.Promotion.DiscountCampaign;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository cho DiscountCampaign entity
 */
@Repository
public interface DiscountCampaignRepository extends BaseRepository<DiscountCampaign, Integer> {

    /**
     * Tìm discount campaigns theo loại
     */
    List<DiscountCampaign> findByCampaignType(String campaignType);

    /**
     * Tìm discount campaigns đang hoạt động
     */
    List<DiscountCampaign> findByIsActiveTrueAndStartDateBeforeAndEndDateAfter(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Tìm discount campaigns sắp bắt đầu
     */
    List<DiscountCampaign> findByStartDateAfter(LocalDateTime startDate);

    /**
     * Tìm discount campaigns theo priority
     */
    List<DiscountCampaign> findByPriority(Integer priority);

    /**
     * Tìm discount campaigns theo priority range
     */
    List<DiscountCampaign> findByPriorityBetween(Integer minPriority, Integer maxPriority);


    /**
     * Tìm discount campaigns theo tên (tìm kiếm)
     */
    List<DiscountCampaign> findByNameContainingIgnoreCase(String name);

    /**
     * Tìm discount campaigns theo mô tả (tìm kiếm)
     */
    List<DiscountCampaign> findByDescriptionContainingIgnoreCase(String description);

    /**
     * Tìm discount campaigns theo trạng thái active
     */
    List<DiscountCampaign> findByIsActive(Boolean isActive);

    /**
     * Tìm discount campaigns theo start date
     */
    List<DiscountCampaign> findByStartDate(LocalDateTime startDate);

    /**
     * Tìm discount campaigns theo start date range
     */
    List<DiscountCampaign> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Tìm discount campaigns theo end date
     */
    List<DiscountCampaign> findByEndDate(LocalDateTime endDate);

    /**
     * Tìm discount campaigns theo end date range
     */
    List<DiscountCampaign> findByEndDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
