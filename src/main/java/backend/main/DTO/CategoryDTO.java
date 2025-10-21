package backend.main.DTO;

import java.time.LocalDateTime;
import java.util.*;

import lombok.Data;

@Data
public class CategoryDTO {
    private Integer id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private Integer displayOrder;
    private Boolean isActive;
    private Integer parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Hierarchy relationships
    private List<CategoryDTO> children = new ArrayList<>();
    private List<CategoryDTO> parents = new ArrayList<>();
    private CategoryDTO parentCategory;
    
    // SEO-friendly URL generation
    public String getSeoUrl() {
        if (parentCategory != null) {
            return "/" + parentCategory.getSlug() + "/" + this.slug;
        }
        return "/" + this.slug;
    }
    
    public String getFullPath() {
        StringBuilder path = new StringBuilder();
        if (parentCategory != null) {
            path.append(parentCategory.getSlug()).append("/");
        }
        path.append(this.slug);
        return path.toString();
    }
}
