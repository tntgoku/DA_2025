package backend.main.Model.Product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_images")
@Getter
@Setter
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Liên kết đến ProductVariant (có thể null)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "product_variant_id", foreignKey = @ForeignKey(name =
    // "FK_productimage_variant"))
    // private ProductVariant productVariant;
    @Column(name = "product_variant_id", nullable = true)
    private Integer variantId = null;
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name =
    // "FK_productimage_product"))
    // private Products product;
    @Column(name = "product_id")
    private Integer productId = 0;
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "image_type", length = 50)
    private String imageType = "gallery";

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "alt_img", columnDefinition = "NVARCHAR(MAX)")
    private String altImg;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
