package backend.main.Request;

public class CateRequest {
    private Integer id; // dùng cho update, create thì có thể null
    private String name; // tên danh mục
    private Integer displayOrder; // thứ tự hiển thị
    private Integer parentId; // id của parent, null nếu là root
    private Boolean active; // trạng thái active/inactive
    private String slug;

    // getter và setter
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

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "CateRequest [id=" + id + ", name=" + name + ", displayOrder=" + displayOrder + ", parentId=" + parentId
                + ", active=" + active + "]";
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

}