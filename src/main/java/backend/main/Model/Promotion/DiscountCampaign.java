package backend.main.Model.Promotion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import backend.main.Model.BaseEntity;

/**
 * Entity cho chiến dịch khuyến mãi tổng thể
 * VD: "Khuyến mãi Tết 2024", "Giảm giá Hè", "Black Friday"
 */
@Entity
@Table(name = "discount_campaigns")
@Getter
@Setter
public class DiscountCampaign extends BaseEntity {

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
    @Column(name = "priority")
    private Integer priority = 0; // Độ ưu tiên khi có nhiều campaign cùng lúc

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
