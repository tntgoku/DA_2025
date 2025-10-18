package backend.main.DTO;

import java.util.Objects;

public class SpecificationDTO {
    private Integer id;
    private Integer categoryId;
    private String name;
    private Integer unitId;

    public SpecificationDTO() {
    }

    public SpecificationDTO(Integer id, Integer categoryId, String name, Integer unitId) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.unitId = unitId;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUnitId() {
        return this.unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public SpecificationDTO id(Integer id) {
        setId(id);
        return this;
    }

    public SpecificationDTO categoryId(Integer categoryId) {
        setCategoryId(categoryId);
        return this;
    }

    public SpecificationDTO name(String name) {
        setName(name);
        return this;
    }

    public SpecificationDTO unitId(Integer unitId) {
        setUnitId(unitId);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SpecificationDTO)) {
            return false;
        }
        SpecificationDTO specificationDTO = (SpecificationDTO) o;
        return Objects.equals(id, specificationDTO.id) && Objects.equals(categoryId, specificationDTO.categoryId)
                && Objects.equals(name, specificationDTO.name) && Objects.equals(unitId, specificationDTO.unitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryId, name, unitId);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", categoryId='" + getCategoryId() + "'" +
                ", name='" + getName() + "'" +
                ", unitId='" + getUnitId() + "'" +
                "}";
    }

}
