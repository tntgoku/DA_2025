package backend.main.Repository;

import backend.main.Model.InventoryHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, Integer> {
    
    // Find by inventory item
    List<InventoryHistory> findByInventoryItemIdOrderByPerformedAtDesc(Integer inventoryItemId);
    
    Page<InventoryHistory> findByInventoryItemIdOrderByPerformedAtDesc(Integer inventoryItemId, Pageable pageable);
    
    // Find by product variant
    List<InventoryHistory> findByProductVariantIdOrderByPerformedAtDesc(Integer productVariantId);
    
    Page<InventoryHistory> findByProductVariantIdOrderByPerformedAtDesc(Integer productVariantId, Pageable pageable);
    
    // Find by action type
    List<InventoryHistory> findByActionTypeOrderByPerformedAtDesc(InventoryHistory.ActionType actionType);
    
    Page<InventoryHistory> findByActionTypeOrderByPerformedAtDesc(InventoryHistory.ActionType actionType, Pageable pageable);
    
    // Find by date range
    List<InventoryHistory> findByPerformedAtBetweenOrderByPerformedAtDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<InventoryHistory> findByPerformedAtBetweenOrderByPerformedAtDesc(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Find by performed by
    List<InventoryHistory> findByPerformedByOrderByPerformedAtDesc(String performedBy);
    
    Page<InventoryHistory> findByPerformedByOrderByPerformedAtDesc(String performedBy, Pageable pageable);
    
    // Find by reference
    List<InventoryHistory> findByReferenceIdAndReferenceTypeOrderByPerformedAtDesc(String referenceId, String referenceType);
    
    // Complex queries
    @Query("SELECT h FROM InventoryHistory h WHERE h.inventoryItem.id = :inventoryId " +
           "AND h.performedAt BETWEEN :startDate AND :endDate " +
           "ORDER BY h.performedAt DESC")
    List<InventoryHistory> findByInventoryAndDateRange(@Param("inventoryId") Integer inventoryId,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT h FROM InventoryHistory h WHERE h.productVariant.id = :variantId " +
           "AND h.actionType = :actionType " +
           "ORDER BY h.performedAt DESC")
    List<InventoryHistory> findByVariantAndActionType(@Param("variantId") Integer variantId,
                                                      @Param("actionType") InventoryHistory.ActionType actionType);
    
    // Statistics queries
    @Query("SELECT SUM(h.quantityChange) FROM InventoryHistory h " +
           "WHERE h.inventoryItem.id = :inventoryId " +
           "AND h.actionType = :actionType " +
           "AND h.performedAt BETWEEN :startDate AND :endDate")
    Long sumQuantityByInventoryAndActionType(@Param("inventoryId") Integer inventoryId,
                                             @Param("actionType") InventoryHistory.ActionType actionType,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT h.actionType, SUM(h.quantityChange) FROM InventoryHistory h " +
           "WHERE h.inventoryItem.id = :inventoryId " +
           "AND h.performedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY h.actionType")
    List<Object[]> getActionTypeSummary(@Param("inventoryId") Integer inventoryId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
    
    // Recent activity
    @Query("SELECT h FROM InventoryHistory h " +
           "WHERE h.performedAt >= :since " +
           "ORDER BY h.performedAt DESC")
    List<InventoryHistory> findRecentActivity(@Param("since") LocalDateTime since);
    
    @Query("SELECT h FROM InventoryHistory h " +
           "WHERE h.performedAt >= :since " +
           "ORDER BY h.performedAt DESC")
    Page<InventoryHistory> findRecentActivity(@Param("since") LocalDateTime since, Pageable pageable);
    
    // Top movers
    @Query("SELECT h.productVariant.id, h.productVariant.product.name, " +
           "SUM(CASE WHEN h.actionType = 'IMPORT' THEN h.quantityChange ELSE 0 END), " +
           "SUM(CASE WHEN h.actionType = 'EXPORT' THEN ABS(h.quantityChange) ELSE 0 END) " +
           "FROM InventoryHistory h " +
           "WHERE h.performedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY h.productVariant.id, h.productVariant.product.name " +
           "ORDER BY (SUM(CASE WHEN h.actionType = 'IMPORT' THEN h.quantityChange ELSE 0 END) + " +
           "SUM(CASE WHEN h.actionType = 'EXPORT' THEN ABS(h.quantityChange) ELSE 0 END)) DESC")
    List<Object[]> getTopMovingProducts(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
    
    // Count by action type
    @Query("SELECT h.actionType, COUNT(h) FROM InventoryHistory h " +
           "WHERE h.performedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY h.actionType")
    List<Object[]> countByActionType(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);
}
