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

    /**
     * Tìm discount targets theo campaign ID
     */
    List<DiscountTarget> findByCampaignId(Integer campaignId);

    /**
     * Tìm discount targets theo loại target
     */
    List<DiscountTarget> findByTargetType(String targetType);

    /**
     * Tìm discount targets theo target type và target ID
     */
    List<DiscountTarget> findByTargetTypeAndTargetId(String targetType, Integer targetId);

    /**
     * Tìm discount targets đang được bao gồm
     */
    List<DiscountTarget> findByIsIncludedTrue();

    /**
     * Tìm discount targets không được bao gồm
     */
    List<DiscountTarget> findByIsIncludedFalse();

    /**
     * Tìm discount targets theo campaign và target type
     */
    List<DiscountTarget> findByCampaignIdAndTargetType(Integer campaignId, String targetType);

}
