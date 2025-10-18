package backend.main.Request.Json;

import java.util.Objects;

import backend.main.DTO.Product.VariantDTO;

public class ItemProductJson {
    private Integer id;
    VariantDTO object;
    Integer quantity;

    public ItemProductJson() {
    }
    public ItemProductJson(Integer id, VariantDTO object, Integer quantity) {
        this.id = id;
        this.object = object;
        this.quantity = quantity;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public VariantDTO getObject() {
        return this.object;
    }

    public void setObject(VariantDTO object) {
        this.object = object;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ItemProductJson id(Integer id) {
        setId(id);
        return this;
    }

    public ItemProductJson object(VariantDTO object) {
        setObject(object);
        return this;
    }

    public ItemProductJson quantity(Integer quantity) {
        setQuantity(quantity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ItemProductJson)) {
            return false;
        }
        ItemProductJson itemProductJson = (ItemProductJson) o;
        return Objects.equals(id, itemProductJson.id) && Objects.equals(object, itemProductJson.object)
                && Objects.equals(quantity, itemProductJson.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, object, quantity);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", object='" + getObject() + "'" +
                ", quantity='" + getQuantity() + "'" +
                "}";
    }

}
