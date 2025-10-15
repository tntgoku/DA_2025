package backend.main.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.*;

import backend.main.Model.Product.ProductVariant;

@Entity
@Table(name = "inventory_items")
@Data
public class InventoryItem extends BaseEntity {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Integer id;

    // @Column(name = "product_variant_id")
    // private Integer productVariant;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;
    @Column(name = "condition_id")
    private Integer condition;
    @Column(name = "source_id")
    private Integer source;
    private String imei;
    private String serialNumber;
    private BigDecimal costPrice;
    private BigDecimal salePrice;
    private BigDecimal listPrice;
    private String status;
    private String position;
    private Integer stock;
    private LocalDateTime importDate;
    private LocalDateTime soldDate;
    private Integer warrantyMonths;
    private String deviceConditionNotes;
    private String previousOwnerInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
