
package backend.main.Request;

import java.util.*;
import backend.main.DTO.*;

public class SpecificationRequest {
    private List<ProductSpecificationDTO> specifications;

    public SpecificationRequest() {
    }

    public SpecificationRequest(List<ProductSpecificationDTO> specifications) {
        this.specifications = specifications;
    }

    public List<ProductSpecificationDTO> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<ProductSpecificationDTO> specifications) {
        this.specifications = specifications;
    }
}