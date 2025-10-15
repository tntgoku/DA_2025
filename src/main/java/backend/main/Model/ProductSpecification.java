package backend.main.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ProductSpecification", schema = "dbo")
public class ProductSpecification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "product_id")
    private Integer product_id;
    @Column(name = "spec_id")
    private Integer spec_id;
    @Column(name = "value")
    private String value;

    public ProductSpecification() {
    }

    public ProductSpecification(Integer id, Integer product_id, Integer spec_id, String value) {
        this.id = id;
        this.product_id = product_id;
        this.spec_id = spec_id;
        this.value = value;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProduct_id() {
        return this.product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getSpec_id() {
        return this.spec_id;
    }

    public void setSpec_id(Integer spec_id) {
        this.spec_id = spec_id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ProductSpecification id(Integer id) {
        setId(id);
        return this;
    }

    public ProductSpecification product_id(Integer product_id) {
        setProduct_id(product_id);
        return this;
    }

    public ProductSpecification spec_id(Integer spec_id) {
        setSpec_id(spec_id);
        return this;
    }

    public ProductSpecification value(String value) {
        setValue(value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProductSpecification)) {
            return false;
        }
        ProductSpecification ProductSpecification = (ProductSpecification) o;
        return Objects.equals(id, ProductSpecification.id)
                && Objects.equals(product_id, ProductSpecification.product_id)
                && Objects.equals(spec_id, ProductSpecification.spec_id)
                && Objects.equals(value, ProductSpecification.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product_id, spec_id, value);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", product_id='" + getProduct_id() + "'" +
                ", spec_id='" + getSpec_id() + "'" +
                ", value='" + getValue() + "'" +
                "}";
    }

}
