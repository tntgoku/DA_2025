package backend.main.Model.Order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import backend.main.Model.BaseEntity;
import backend.main.Model.Promotion.Voucher;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Integer id;

    @Column(name = "order_code", nullable = false, unique = true, length = 50)
    private String orderCode;

    // Liên kết tới User (customer)
    // Trong class Order:
    @Column(name = "customer_id",nullable = true)
    private Integer customer;

    @Column(name = "customer_name", length = 255)
    private String customerName;

    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Column(name = "customer_email", length = 100)
    private String customerEmail;

    @Column(name = "customer_address", columnDefinition = "NVARCHAR(MAX)")
    private String customerAddress;

    @Column(name = "order_status", length = 50)
    private String orderStatus = "pending"; // pending, confirmed, processing, shipped, delivered, cancelled, returned

    @Column(name = "payment_status", length = 50)
    private String paymentStatus = "unpaid"; // unpaid, paid, partial, refunded

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "subtotal_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotalAmount;

    @Column(name = "discount_amount", precision = 18, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "shipping_fee", precision = 18, scale = 2)
    private BigDecimal shippingFee = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 18, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "amount_paid", precision = 18, scale = 2)
    private BigDecimal amountPaid = BigDecimal.ZERO;

    // Liên kết voucher
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    @JsonIgnore
    private Voucher voucher;

    @Column(name = "voucher_discount", precision = 18, scale = 2)
    private BigDecimal voucherDiscount = BigDecimal.ZERO;

    @Column(name = "shipping_address", columnDefinition = "NVARCHAR(MAX)")
    private String shippingAddress;

    @Column(name = "shipping_method", length = 100)
    private String shippingMethod;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "notes", columnDefinition = "NVARCHAR(MAX)")
    private String notes;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Danh sách OrderItem
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<OrderItem> orderItems = new HashSet<>();
}
