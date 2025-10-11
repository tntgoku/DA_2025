package backend.main.Request.Json;

import java.util.List;

import backend.main.Request.VariantRequest;

public class VariantColorJson {
    private String idColor;
    private String color;
    private List<VariantRequest> variantsStorage;

    public String getIdColor() {
        return idColor;
    }

    public void setIdColor(String idColor) {
        this.idColor = idColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<VariantRequest> getVariantsStorage() {
        return variantsStorage;
    }

    public void setVariantsStorage(List<VariantRequest> variantsStorage) {
        this.variantsStorage = variantsStorage;
    }

    @Override
    public String toString() {
        return "VariantColorJson [idColor=" + idColor + ", color=" + color + ", variantsStorage=" + variantsStorage
                + "]";
    }

}
