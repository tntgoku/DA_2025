package backend.main.DTO.Category;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO cho thống kê danh mục
 */
@Getter
@Setter
public class CategoryStatsDTO {
    private Integer id;
    private String name;
    private String slug;
    private Integer productCount;
    private Integer childCategoryCount;
    private Integer totalProductsInTree; // Tổng sản phẩm trong toàn bộ cây
    private Integer activeProductCount;
    private Integer inactiveProductCount;
    private LocalDateTime lastProductAdded;
    private LocalDateTime lastUpdated;
    
    public CategoryStatsDTO() {
    }

    public CategoryStatsDTO(Integer id, String name, String slug, Integer productCount, 
                           Integer childCategoryCount, Integer totalProductsInTree, 
                           Integer activeProductCount, Integer inactiveProductCount, 
                           LocalDateTime lastProductAdded, LocalDateTime lastUpdated) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.productCount = productCount;
        this.childCategoryCount = childCategoryCount;
        this.totalProductsInTree = totalProductsInTree;
        this.activeProductCount = activeProductCount;
        this.inactiveProductCount = inactiveProductCount;
        this.lastProductAdded = lastProductAdded;
        this.lastUpdated = lastUpdated;
    }
}
