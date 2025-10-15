package backend.main.Model.Product;

import backend.main.Model.BaseEntity;
import backend.main.Model.User.Supplier;
import backend.main.Model.User.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "product_sources")
public class ProductSource extends BaseEntity {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Long id;

    private String sourceType;
    private LocalDateTime sourceDate;
    private String referenceNumber;
    private BigDecimal totalAmount;
    private String notes;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;
}
