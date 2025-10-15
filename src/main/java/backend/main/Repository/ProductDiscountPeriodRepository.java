package backend.main.Repository;

import backend.main.Model.Promotion.ProductDiscountPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDiscountPeriodRepository extends JpaRepository<ProductDiscountPeriod, Long> {
    
    /**
     * Tìm tất cả product discounts của một period
     */
    List<ProductDiscountPeriod> findByDiscountPeriodId(Long periodId);
    
    /**
     * Tìm tất cả product discounts của một sản phẩm
     */
    List<ProductDiscountPeriod> findByProductId(Long productId);
    
    /**
     * Tìm product discount cụ thể
     */
    Optional<ProductDiscountPeriod> findByProductIdAndDiscountPeriodId(Long productId, Long periodId);
    
    /**
     * Xóa tất cả product discounts của một period
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductDiscountPeriod pdp WHERE pdp.discountPeriodId = :periodId")
    void deleteByDiscountPeriodId(@Param("periodId") Long periodId);
    
    /**
     * Lấy các sản phẩm có giảm giá và đang được included
     */
    @Query("SELECT pdp FROM ProductDiscountPeriod pdp WHERE pdp.discountPeriodId = :periodId AND pdp.included = true")
    List<ProductDiscountPeriod> findIncludedProductsByPeriodId(@Param("periodId") Long periodId);
}

