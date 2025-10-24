package backend.main.DTO.Category;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO cho cây danh mục với cấu trúc phân cấp
 */
@Getter
@Setter
public class CategoryTreeDTO {
    private Integer id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private Integer parentId;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Cấu trúc cây
    private List<CategoryTreeDTO> children = new ArrayList<>();
    private CategoryTreeDTO parent;
    
    // Thống kê
    private Integer productCount = 0;
    private Integer childCount = 0;
    
    // SEO
    private String fullPath;
    private String seoUrl;
    
    public CategoryTreeDTO() {
    }

    public CategoryTreeDTO(Integer id, String name, String slug, String description, 
                          String imageUrl, Integer parentId, Integer displayOrder, 
                          Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.imageUrl = imageUrl;
        this.parentId = parentId;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public void addChild(CategoryTreeDTO child) {
        this.children.add(child);
        child.setParent(this);
        this.childCount = this.children.size();
    }
    
    public String getFullPath() {
        if (parent != null) {
            return parent.getFullPath() + "/" + this.slug;
        }
        return "/" + this.slug;
    }
    
    public String getSeoUrl() {
        if (parent != null) {
            return "/" + parent.getSlug() + "/" + this.slug;
        }
        return "/" + this.slug;
    }
}
