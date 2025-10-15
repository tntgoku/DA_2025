package backend.main.Model.Promotion;

import jakarta.persistence.*;
import lombok.Data;
import backend.main.Model.BaseEntity;

import java.time.LocalDateTime;

/**
 * Entity cho việc áp dụng giảm giá theo sản phẩm trong đợt giảm giá
 */
@Entity
@Table(name = "product_discount_periods")
@Data
public class ProductDiscountPeriod extends BaseEntity {

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "discount_period_id", nullable = false)
    private Long discountPeriodId;

    @Column(name = "percentage_value", nullable = false)
    private Integer percentageValue; // Giá trị % giảm giá

    @Column(name = "included")
    private Boolean included = true; // Có áp dụng không

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
