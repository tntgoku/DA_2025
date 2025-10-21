package backend.main.Repository;

import backend.main.Model.Promotion.DiscountCampaign;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiscountCampaignRepository extends BaseRepository<DiscountCampaign, Integer> {

    List<DiscountCampaign> findByCampaignType(String campaignType);

    List<DiscountCampaign> findByIsActiveTrueAndStartDateBeforeAndEndDateAfter(LocalDateTime startDate,
            LocalDateTime endDate);

    List<DiscountCampaign> findByStartDateAfter(LocalDateTime startDate);

    List<DiscountCampaign> findByPriority(Integer priority);

    List<DiscountCampaign> findByPriorityBetween(Integer minPriority, Integer maxPriority);

    List<DiscountCampaign> findByNameContainingIgnoreCase(String name);

    List<DiscountCampaign> findByDescriptionContainingIgnoreCase(String description);

    List<DiscountCampaign> findByIsActive(Boolean isActive);

    List<DiscountCampaign> findByStartDate(LocalDateTime startDate);

    List<DiscountCampaign> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<DiscountCampaign> findByEndDate(LocalDateTime endDate);

    List<DiscountCampaign> findByEndDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
