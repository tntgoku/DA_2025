package backend.main.Model.Promotion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "discount_targets")
@Entity
@Data
public class DiscountTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "discount_id")
    private Integer discountId;
    @Column(name = "target_type")
    private String targetType;
    @Column(name = "target_id")
    private Integer tartgetId;
}
