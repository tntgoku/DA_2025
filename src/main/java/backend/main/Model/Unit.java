package backend.main.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Unit", schema = "dbo")
public class Unit {
    @Id
    @Column(name = "id")
    private Integer key;
    @Column(name = "name")
    private String value;

    public Unit() {
    }

    public Unit(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Unit [key=" + key + ", value=" + value + "]";
    }

}