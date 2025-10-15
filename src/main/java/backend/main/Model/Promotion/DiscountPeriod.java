package backend.main.Model.Promotion;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import io.hypersistence.utils.hibernate.type.json.JsonType;

import java.time.LocalDateTime;
import backend.main.Model.BaseEntity;

/**
 * Entity cho Đợt giảm giá với các quy tắc nâng cao
 */
@Entity
@Table(name = "discount_periods")
@Data
public class DiscountPeriod extends BaseEntity {

    @Column(name = "discount_period_code", unique = true, nullable = false, length = 50)
    private String discountPeriodCode;

    @Column(name = "discount_period_name", nullable = false, length = 255)
    private String discountPeriodName;

    @Column(name = "min_percentage_value")
    private Integer minPercentageValue;

    @Column(name = "max_percentage_value")
    private Integer maxPercentageValue;

    // Quy tắc áp dụng cho hóa đơn
    @Column(name = "order_min_total")
    private Long orderMinTotal;

    @Column(name = "order_min_items")
    private Integer orderMinItems;

    @Column(name = "order_discount_type", length = 20)
    private String orderDiscountType; // 'percent' or 'fixed'

    @Column(name = "order_discount_value")
    private Long orderDiscountValue;

    @Column(name = "order_gift_description", length = 500)
    private String orderGiftDescription;

    @Column(name = "enable_order_rule")
    private Boolean enableOrderRule = false;

    // Các quy tắc nâng cao (lưu dạng JSON)
    @Type(JsonType.class)
    @Column(name = "tiered_rules", columnDefinition = "jsonb")
    private String tieredRules;

    @Type(JsonType.class)
    @Column(name = "buy_x_get_y_rules", columnDefinition = "jsonb")
    private String buyXGetYRules;

    @Type(JsonType.class)
    @Column(name = "bundle_rules", columnDefinition = "jsonb")
    private String bundleRules;

    @Type(JsonType.class)
    @Column(name = "unique_product_rule", columnDefinition = "jsonb")
    private String uniqueProductRule;

    @Type(JsonType.class)
    @Column(name = "gift_rules", columnDefinition = "jsonb")
    private String giftRules;

    @Type(JsonType.class)
    @Column(name = "free_shipping_rule", columnDefinition = "jsonb")
    private String freeShippingRule;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "status")
    private Integer status = 1; // 1: active, 0: inactive

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
