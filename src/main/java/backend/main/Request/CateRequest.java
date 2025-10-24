package backend.main.Request;

import java.time.LocalDateTime;
import java.util.Objects;

public class CateRequest {
    private Integer id;
    private String name;
    private Integer displayOrder;
    private Integer parentId;
    private Boolean isActive;
    private String slug;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public CateRequest() {
    }

    public CateRequest(Integer id, String name, Integer displayOrder, Integer parentId, Boolean isActive, String slug,
            LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.displayOrder = displayOrder;
        this.parentId = parentId;
        this.isActive = isActive;
        this.slug = slug;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Boolean isIsActive() {
        return this.isActive;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getSlug() {
        return this.slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CateRequest id(Integer id) {
        setId(id);
        return this;
    }

    public CateRequest name(String name) {
        setName(name);
        return this;
    }

    public CateRequest displayOrder(Integer displayOrder) {
        setDisplayOrder(displayOrder);
        return this;
    }

    public CateRequest parentId(Integer parentId) {
        setParentId(parentId);
        return this;
    }

    public CateRequest isActive(Boolean isActive) {
        setIsActive(isActive);
        return this;
    }

    public CateRequest slug(String slug) {
        setSlug(slug);
        return this;
    }

    public CateRequest updatedAt(LocalDateTime updatedAt) {
        setUpdatedAt(updatedAt);
        return this;
    }

    public CateRequest createdAt(LocalDateTime createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CateRequest)) {
            return false;
        }
        CateRequest cateRequest = (CateRequest) o;
        return Objects.equals(id, cateRequest.id) && Objects.equals(name, cateRequest.name)
                && Objects.equals(displayOrder, cateRequest.displayOrder)
                && Objects.equals(parentId, cateRequest.parentId) && Objects.equals(isActive, cateRequest.isActive)
                && Objects.equals(slug, cateRequest.slug) && Objects.equals(updatedAt, cateRequest.updatedAt)
                && Objects.equals(createdAt, cateRequest.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, displayOrder, parentId, isActive, slug, updatedAt, createdAt);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", displayOrder='" + getDisplayOrder() + "'" +
                ", parentId='" + getParentId() + "'" +
                ", isActive='" + isIsActive() + "'" +
                ", slug='" + getSlug() + "'" +
                ", updatedAt='" + getUpdatedAt() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                "}";
    }

}