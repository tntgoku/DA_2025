package backend.main.Model.Order;

import jakarta.persistence.*;

import backend.main.Model.Product.ProductVariant;
import backend.main.Model.InventoryItem;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Liên kết đến Order (cha)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;
    // Liên kết tới ProductVariant (có thể null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    @JsonIgnore
    private ProductVariant variant;
    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;
    @Column(name = "variant_attributes", nullable = false, length = 500)
    private String variantAttributes;
    @Column(name = "sku", nullable = false, length = 100)
    private String sku;
    @Column(name = "condition_type", nullable = true, length = 50)
    private String conditionType;
    @Column(name = "imei", length = 50)
    private String imei;
    @Column(name = "unit_sale_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitSalePrice;
    @Column(name = "unit_cost_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitCostPrice;
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;
    @Column(name = "discount_amount", precision = 18, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    @Column(name = "total_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalPrice;
    @Column(name = "warranty_months")
    private Integer warrantyMonths;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
