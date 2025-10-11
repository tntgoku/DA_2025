package backend.main.Model;

import jakarta.persistence.*;
import backend.main.Model.Product.*;

@Entity
@Table(name = "product_specs")
public class ProductSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Liên kết với Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    // Liên kết với SpecGroup
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spec_group_id", nullable = false)
    private SpecGroup specGroup;

    @Column(name = "spec_name", nullable = false, length = 255)
    private String specName;

    @Column(name = "spec_value", nullable = false, length = 255)
    private String specValue;

    @Column(name = "spec_unit", length = 50)
    private String specUnit;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public SpecGroup getSpecGroup() {
        return specGroup;
    }

    public void setSpecGroup(SpecGroup specGroup) {
        this.specGroup = specGroup;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecValue() {
        return specValue;
    }

    public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }

    public String getSpecUnit() {
        return specUnit;
    }

    public void setSpecUnit(String specUnit) {
        this.specUnit = specUnit;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
