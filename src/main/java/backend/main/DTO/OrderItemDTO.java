package backend.main.DTO;

import backend.main.DTO.Product.VariantDTO;
import java.util.Objects;

public class OrderItemDTO {
    private Integer id;
    private VariantDTO object;
    private Integer quantity;

    public OrderItemDTO() {
    }

    public OrderItemDTO(Integer id, VariantDTO object, Integer quantity) {
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

    public OrderItemDTO id(Integer id) {
        setId(id);
        return this;
    }

    public OrderItemDTO object(VariantDTO object) {
        setObject(object);
        return this;
    }

    public OrderItemDTO quantity(Integer quantity) {
        setQuantity(quantity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderItemDTO)) {
            return false;
        }
        OrderItemDTO orderItemDTO = (OrderItemDTO) o;
        return Objects.equals(id, orderItemDTO.id) && Objects.equals(object, orderItemDTO.object)
                && Objects.equals(quantity, orderItemDTO.quantity);
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
