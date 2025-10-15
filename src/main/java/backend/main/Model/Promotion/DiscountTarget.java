package backend.main.Model.Promotion;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entity áp dụng giảm giá cho sản phẩm cụ thể
 * - targetType = 'PRODUCT': Áp dụng cho toàn bộ sản phẩm
 * - targetType = 'VARIANT': Áp dụng cho variant cụ thể (màu, dung lượng...)
 * - targetType = 'CATEGORY': Áp dụng cho cả danh mục
 */
@Table(name = "discount_targets")
@Entity
@Data
public class DiscountTarget {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * ID của campaign áp dụng
     */
    @Column(name = "campaign_id", nullable = false)
    private Integer campaignId;
    
    /**
     * Loại target: 'PRODUCT', 'VARIANT', 'CATEGORY'
     */
    @Column(name = "target_type", nullable = false, length = 20)
    private String targetType;
    
    /**
     * ID của target (product_id, variant_id, category_id)
     */
    @Column(name = "target_id", nullable = false)
    private Integer targetId;
    
    /**
     * Phần trăm giảm giá cho target này
     */
    @Column(name = "percentage_value")
    private Integer percentageValue = 0;
    
    /**
     * Có áp dụng không (cho phép bật/tắt từng sản phẩm)
     */
    @Column(name = "is_included")
    private Boolean isIncluded = true;
    
    /**
     * Giá trị giảm cố định (nếu không dùng %)
     */
    @Column(name = "fixed_discount_value")
    private Integer fixedDiscountValue;
    
    /**
     * Số lượng tối thiểu để được giảm giá
     */
    @Column(name = "min_quantity")
    private Integer minQuantity = 1;
    
    /**
     * Số lượng tối đa được giảm giá
     */
    @Column(name = "max_quantity")
    private Integer maxQuantity;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
