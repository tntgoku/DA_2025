package backend.main.Model.Promotion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity cho mã giảm giá (Voucher code)
 * Khách hàng nhập mã này khi checkout để được giảm giá
 */
@Entity
@Table(name = "vouchers")
@Getter
@Setter
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code; // Mã voucher: VD "SUMMER2024", "TET15"

    @Column(name = "name", nullable = false, length = 255)
    private String name; // Tên voucher

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description; // Mô tả chi tiết

    @Column(name = "discount_type", nullable = false, length = 50)
    private String discountType; // 'percentage' or 'fixed_amount'

    @Column(name = "value", nullable = false, precision = 10, scale = 2)
    private BigDecimal value; // Giá trị giảm (% hoặc số tiền)

    @Column(name = "max_discount", precision = 10, scale = 2)
    private BigDecimal maxDiscount; // Giảm tối đa (nếu discount_type = percentage)

    @Column(name = "min_order_value", precision = 10, scale = 2)
    private BigDecimal minOrderValue; // Giá trị đơn hàng tối thiểu

    @Column(name = "total_uses")
    private Integer totalUses = 0; // Số lần đã sử dụng

    @Column(name = "max_uses")
    private Integer maxUses; // Số lần sử dụng tối đa (null = không giới hạn)

    @Column(name = "max_uses_per_user")
    private Integer maxUsesPerUser; // Số lần mỗi user được dùng

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "is_active")
    private Boolean isActive = true;
    /**
     * Priority khi có nhiều voucher
     */
    @Column(name = "priority")
    private Integer priority = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    @JsonIgnore
    private DiscountCampaign campaign;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
