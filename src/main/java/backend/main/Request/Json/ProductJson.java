package backend.main.Request.Json;

import java.util.List;

import backend.main.Model.ProductSpecfication;

public class ProductJson {
    private Integer id;
    private String name;
    private Integer category;
    private String productType;
    private String slug;
    private String description;
    private String brand;
    private String model;
    private Boolean isActive;
    private Boolean isFeatured;
    private Boolean isHot;
    private String featuredImageIndex;
    private List<ProductSpecfication> specifications;
    private List<ImageJson> images;
    private List<VariantColorJson> variants;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public String getFeaturedImageIndex() {
        return featuredImageIndex;
    }

    public void setFeaturedImageIndex(String featuredImageIndex) {
        this.featuredImageIndex = featuredImageIndex;
    }

    public List<ImageJson> getImages() {
        return images;
    }

    public void setImages(List<ImageJson> images) {
        this.images = images;
    }

    public List<VariantColorJson> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantColorJson> variants) {
        this.variants = variants;
    }

    @Override
    public String toString() {
        return "ProductJson [id=" + id + ", name=" + name + ", category=" + category + ", productType=" + productType
                + ", slug=" + slug + ", description=" + description + ", brand=" + brand + ", model=" + model
                + ", specifications=" + (specifications == null ? -1 : specifications.size()) + ", isActive=" + isActive
                + ", isFeatured=" + isFeatured
                + ", isHot=" + isHot + ", featuredImageIndex=" + featuredImageIndex + ", images=" + images
                + ", variants=" + variants + "]";
    }

    public List<ProductSpecfication> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<ProductSpecfication> specifications) {
        this.specifications = specifications;
    }

}
