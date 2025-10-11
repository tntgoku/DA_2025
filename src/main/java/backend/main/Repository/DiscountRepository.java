package backend.main.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import backend.main.DTO.PromotionDTO.PromotionDTO;
import backend.main.Model.Promotion.Discount;
import java.util.*;

@Repository
public interface DiscountRepository extends BaseRepository<Discount, Integer> {
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

}
