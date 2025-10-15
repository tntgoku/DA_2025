package backend.main.Model.Promotion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity cho chiến dịch khuyến mãi tổng thể
 * VD: "Khuyến mãi Tết 2024", "Giảm giá Hè", "Black Friday"
 */
@Entity
@Table(name = "discount_campaigns")
@Getter
@Setter
public class DiscountCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "campaign_type", nullable = false, length = 50)
    private String campaignType; // 'SALE', 'HOT', 'FEATURED', 'SEASONAL', 'TET', 'SUMMER'

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "banner_image_url", length = 500)
    private String bannerImageUrl;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // ============ QUY TẮC NÂNG CAO (Lưu dạng JSON trong NVARCHAR(MAX)) ============
    
    /**
     * Quy tắc giảm giá theo bậc thang
     * JSON Format: [{"minAmount": 1000000, "discountType": "fixed", "discountValue": 100000, "maxDiscount": null}, ...]
     */
    @Column(name = "tiered_rules", columnDefinition = "NVARCHAR(MAX)")
    private String tieredRules;

    /**
     * Quy tắc mua X tặng Y
     * JSON Format: [{"buyProductIds": ["1","2"], "buyQuantity": 2, "getProductIds": ["10"], "getQuantity": 1, "getDiscountPercent": 100}, ...]
     */
    @Column(name = "buy_x_get_y_rules", columnDefinition = "NVARCHAR(MAX)")
    private String buyXGetYRules;

    /**
     * Quy tắc combo/bundle
     * JSON Format: [{"requiredProducts": [{"productId":"1","minQuantity":1}], "discountType": "percent", "discountValue": 10}, ...]
     */
    @Column(name = "bundle_rules", columnDefinition = "NVARCHAR(MAX)")
    private String bundleRules;

    /**
     * Quy tắc giảm giá khi mua nhiều sản phẩm khác nhau
     * JSON Format: {"minUniqueProducts": 3, "discountType": "percent", "discountValue": 15, "maxDiscount": 500000}
     */
    @Column(name = "unique_product_rule", columnDefinition = "NVARCHAR(MAX)")
    private String uniqueProductRule;

    /**
     * Quy tắc quà tặng
     * JSON Format: [{"minAmount": 5000000, "minItems": 0, "gifts": [{"description": "Tai nghe", "estimatedValue": 500000}]}, ...]
     */
    @Column(name = "gift_rules", columnDefinition = "NVARCHAR(MAX)")
    private String giftRules;

    /**
     * Quy tắc miễn phí vận chuyển
     * JSON Format: {"minAmount": 500000, "maxShippingDiscount": 30000}
     */
    @Column(name = "free_shipping_rule", columnDefinition = "NVARCHAR(MAX)")
    private String freeShippingRule;

    // ============ QUY TẮC ÁP DỤNG CHO HÓA ĐƠN ============
    
    @Column(name = "min_order_amount")
    private Integer minOrderAmount; // Giá trị đơn hàng tối thiểu

    @Column(name = "min_items_count")
    private Integer minItemsCount; // Số lượng sản phẩm tối thiểu

    @Column(name = "order_discount_type", length = 20)
    private String orderDiscountType; // 'percent' or 'fixed'

    @Column(name = "order_discount_value")
    private Integer orderDiscountValue; // Giá trị giảm cho hóa đơn

    @Column(name = "order_gift_description", columnDefinition = "NVARCHAR(500)")
    private String orderGiftDescription; // Mô tả quà tặng cho đơn hàng

    @Column(name = "priority")
    private Integer priority = 0; // Độ ưu tiên khi có nhiều campaign cùng lúc

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Liên kết 1-Nhiều với Voucher
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voucher> vouchers;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
