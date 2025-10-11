package backend.main.DTO.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class VariantDTO {
    private Integer variantId;
    private Integer productId;
    private String nameVariants;
    private String sku;
    private String color;
    private String colorCode;
    private String storage;
    private String ram;
    private String regionCode;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal price;
    private BigDecimal sale_price;
    private BigDecimal list_price;
    private BigDecimal discount;
    private Integer warrantly;
    private Integer stock;
    private String status;
}
