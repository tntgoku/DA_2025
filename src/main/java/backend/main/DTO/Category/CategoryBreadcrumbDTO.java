package backend.main.DTO.Category;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO cho breadcrumb danh mục
 */
@Getter
@Setter
public class CategoryBreadcrumbDTO {
    private Integer id;
    private String name;
    private String slug;
    private String url;
    private Integer level;
    private Boolean isLast;
    
    public CategoryBreadcrumbDTO() {
    }

    public CategoryBreadcrumbDTO(Integer id, String name, String slug, String url, Integer level, Boolean isLast) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.url = url;
        this.level = level;
        this.isLast = isLast;
    }
}
