package backend.main.DTO;

import java.util.*;

import lombok.Data;

@Data
public class CategoryDTO {
    private Integer id;
    private String name;
    private Integer displayOrder;
    private boolean isActive;
    private Integer parentId;
    private List<CategoryDTO> Parents = new ArrayList<>();

}
