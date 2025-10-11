package backend.main.DTO;

import lombok.Data;

@Data
public class ImageDTO {
    private Integer id;
    private String imgSrc;
    private String imgAlt;
    private Boolean isPrimary;
    private Integer displayOrder;

}
