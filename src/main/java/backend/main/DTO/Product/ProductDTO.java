package backend.main.DTO.Product;

import java.time.LocalDateTime;
import java.util.*;

import backend.main.DTO.ImageDTO;
import lombok.Data;

@Data
public class ProductDTO {
    private Integer id;
    private Integer category;
    private String productType;
    private String name;
    private String slug;
    private String description;
    private String brand;
    private String model;
    private Boolean isActive;
    private Boolean isFeatured;
    private Boolean isHot;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<VariantDTO> variants;
    private List<ProductSpecificationDTO> specifications;
    private List<ImageDTO> images;
}
