package backend.main.Model.Product;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import backend.main.Model.BaseEntity;
import backend.main.Model.InventoryItem;

@Entity
@Table(name = "product_variants")
public class ProductVariant extends BaseEntity {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Products product;
    @Column(name = "name_variants")
    private String nameVariants;

    @Column(nullable = false, unique = true, length = 100)
    private String sku;

    @Column(nullable = false, length = 50)
    private String color;

    @Column(name = "color_code", length = 20)
    private String colorCode;

    @Column(nullable = true, length = 255)
    private String storage;

    @Column(length = 20)
    private String ram;

    @Column(name = "region_code", length = 20)
    private String regionCode;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @OneToOne(mappedBy = "productVariant", cascade = CascadeType.ALL, // Hoặc CascadeType.REMOVE
            orphanRemoval = true // Rất tốt để giữ cho DB sạch
    )
    private InventoryItem inventoryItem;

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    // Getters và Setters
    // public Integer getId() {
    // return id;
    // }

    // public void setId(Integer id) {
    // this.id = id;
    // }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public String getNameVariants() {
        return nameVariants;
    }

    public void setNameVariants(String nameVariants) {
        this.nameVariants = nameVariants;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
