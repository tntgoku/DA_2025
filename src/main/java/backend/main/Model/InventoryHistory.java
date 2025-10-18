package backend.main.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

import backend.main.Model.Product.ProductVariant;

import java.math.BigDecimal;

@Entity
@Table(name = "inventory_history")
@Data
public class InventoryHistory extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem inventoryItem;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;
    
    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;
    
    @Column(name = "quantity_before")
    private Integer quantityBefore;
    
    @Column(name = "quantity_after")
    private Integer quantityAfter;
    
    @Column(name = "reason")
    private String reason;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "reference_id")
    private String referenceId; // Order ID, Purchase ID, etc.
    
    @Column(name = "reference_type")
    private String referenceType; // "order", "purchase", "adjustment", etc.
    
    @Column(name = "unit_cost")
    private BigDecimal unitCost;
    
    @Column(name = "total_cost")
    private BigDecimal totalCost;
    
    @Column(name = "performed_by")
    private String performedBy; // User ID or username
    
    @Column(name = "performed_at", nullable = false)
    private LocalDateTime performedAt;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "supplier")
    private String supplier;
    
    @Column(name = "batch_number")
    private String batchNumber;
    
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
    
    // Enums
    public enum ActionType {
        IMPORT("Nhập kho"),
        EXPORT("Xuất kho"),
        ADJUSTMENT("Điều chỉnh"),
        TRANSFER("Chuyển kho"),
        RETURN("Trả hàng"),
        DAMAGE("Hỏng hóc"),
        LOSS("Mất mát"),
        AUDIT("Kiểm kê");
        
        private final String description;
        
        ActionType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // Constructors
    public InventoryHistory() {
        this.performedAt = LocalDateTime.now();
    }
    
    public InventoryHistory(InventoryItem inventoryItem, ActionType actionType, 
                           Integer quantityChange, Integer quantityBefore, Integer quantityAfter) {
        this();
        this.inventoryItem = inventoryItem;
        this.productVariant = inventoryItem.getProductVariant();
        this.actionType = actionType;
        this.quantityChange = quantityChange;
        this.quantityBefore = quantityBefore;
        this.quantityAfter = quantityAfter;
    }
    
    // Helper methods
    public boolean isImport() {
        return ActionType.IMPORT.equals(actionType);
    }
    
    public boolean isExport() {
        return ActionType.EXPORT.equals(actionType);
    }
    
    public boolean isAdjustment() {
        return ActionType.ADJUSTMENT.equals(actionType);
    }
    
    public String getActionDescription() {
        return actionType != null ? actionType.getDescription() : "";
    }
    
    public BigDecimal calculateTotalCost() {
        if (unitCost != null && quantityChange != null) {
            return unitCost.multiply(BigDecimal.valueOf(Math.abs(quantityChange)));
        }
        return BigDecimal.ZERO;
    }
}
