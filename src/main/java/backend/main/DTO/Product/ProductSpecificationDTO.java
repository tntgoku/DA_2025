package backend.main.DTO.Product;

public class ProductSpecificationDTO {
    private int id;
    private int productId;
    private int specId;
    private String value;
    private String label;
    private String unitName;

    @Override
    public String toString() {
        return "ProductSpecificationDTO [id=" + id + ", productId=" + productId + ", specId=" + specId + ", value="
                + value + ", label=" + label + ", unitName=" + unitName + "]";
    }

    public ProductSpecificationDTO() {
    }

    public ProductSpecificationDTO(int id, int productId, int specId, String value, String label, String unitName) {
        this.id = id;
        this.productId = productId;
        this.specId = specId;
        this.value = value;
        this.label = label;
        this.unitName = unitName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

}
