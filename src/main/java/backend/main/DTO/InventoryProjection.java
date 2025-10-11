package backend.main.DTO;

import java.math.BigDecimal;

public interface InventoryProjection {

    // ===== ProductVariant =====
    Integer getVariantId();

    String getNameVariants();

    String getSku();

    String getRegionCode();

    Boolean getIsActive();

    // ===== Product =====
    Integer getProductId();

    String getProductName();

    // ===== InventoryItem =====
    Integer getInventoryId();

    Integer getCondition();

    Integer getSource();

    BigDecimal getCostPrice();

    BigDecimal getSalePrice();

    BigDecimal getListPrice();

    String getStatus();

    Integer getStock();

    Integer getWarrantyMonths();

    String getDeviceConditionNotes();

}
