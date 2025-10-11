package backend.main.Model;

public class ProductSpecfication {
    private Integer id;
    private Integer productId;
    private Integer specId;
    private String unitName;
    private String label;
    private String value;

    @Override
    public String toString() {
        return "ProductSpecification [" +
                "id=" + (id != null ? id : "null") +
                ", productId=" + (productId != null ? productId : "null") +
                ", specId=" + (specId != null ? specId : "null") +
                ", unitName=" + (unitName != null ? unitName : "") +
                ", label=" + (label != null ? label : "") +
                ", value=" + (value != null ? value : "") +
                "]";
    }

    public ProductSpecfication() {
    }

    public ProductSpecfication(Integer id, Integer productId, Integer specId, String unitName, String label,
            String value) {
        this.id = id;
        this.productId = productId;
        this.specId = specId;
        this.unitName = unitName;
        this.label = label;
        this.value = value;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getSpecId() {
        return specId;
    }

    public void setSpecId(Integer specId) {
        this.specId = specId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
