package backend.main.Request;

import java.util.*;

import backend.main.Model.ProductSpecfication;

public class ProductRequest {
    private Integer id;
    private Integer categoryId;
    private String productType;
    private String slug;
    private String name;
    private String description;
    private String brand;
    private String model;
    private List<ProductSpecfication> specifications;
    private Boolean isActive;
    private Boolean isFeatured;
    private Boolean isHot;
    private String featuredImageIndex;
    private List<VariantRequest> variants;

    // Getters and Setters
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ProductSpecfication> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<ProductSpecfication> specifications) {
        this.specifications = specifications;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    @Override
    public String toString() {
        return "ProductRequest [categoryId=" + categoryId + ", productType=" + productType + ", name=" + name
                + ", description=" + description + ", brand=" + brand + ", model=" + model + ", specifications="
                + (specifications == null ? -1 : specifications.size()) + ", isActive=" + isActive + ", isFeatured="
                + isFeatured + ", isHot=" + isHot + "]";
    }

    public List<VariantRequest> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantRequest> variants) {
        this.variants = variants;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

}
