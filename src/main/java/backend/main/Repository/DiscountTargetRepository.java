package backend.main.Repository;

import backend.main.Model.Promotion.DiscountTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository cho DiscountTarget entity
 */
@Repository
public interface DiscountTargetRepository extends JpaRepository<DiscountTarget, Integer> {

    List<DiscountTarget> findByCampaignId(Integer campaignId);

    List<DiscountTarget> findByTargetType(String targetType);

    List<DiscountTarget> findByTargetTypeAndTargetId(String targetType, Integer targetId);

    List<DiscountTarget> findByIsIncludedTrue();

    List<DiscountTarget> findByIsIncludedFalse();

    List<DiscountTarget> findByCampaignIdAndTargetType(Integer campaignId, String targetType);

}
