package backend.main.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Specification", schema = "dbo")
public class Specification extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "unit_id", nullable = true)
    private Integer unitId;

    public Specification() {
    }

    public Specification(Integer id, String name, Integer unit) {
        this.setId(id);
        this.name = name;
        this.unitId = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Specification [id=" + (getId() == null ? -1 : 0)
                + ", name=" + name + ", unitId=" + (unitId == null ? -1 : unitId) + "]";
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

}
