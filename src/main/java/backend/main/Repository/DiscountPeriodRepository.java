package backend.main.Repository;

import backend.main.Model.Promotion.DiscountPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountPeriodRepository extends JpaRepository<DiscountPeriod, Long> {

    /**
     * Tìm discount period theo code
     */
    Optional<DiscountPeriod> findByDiscountPeriodCode(String code);

    /**
     * Tìm các discount periods đang active trong khoảng thời gian
     */
    @Query("SELECT dp FROM DiscountPeriod dp WHERE dp.status = 1 AND dp.startTime <= :now AND dp.endTime >= :now")
    List<DiscountPeriod> findActivePeriodsAt(LocalDateTime now);

    /**
     * Tìm các discount periods theo status
     */
    List<DiscountPeriod> findByStatus(Integer status);

    /**
     * Kiểm tra xem code đã tồn tại chưa
     */
    boolean existsByDiscountPeriodCode(String code);
}
