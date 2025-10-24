package backend.main.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.main.DTO.InventoryProjection;
import backend.main.Model.Product.ProductVariant;

@Repository
public interface VariantReponsitory extends BaseRepository<ProductVariant, Integer> {
    @Query("""
            SELECT
                pv.id AS variantId,
                pv.nameVariants AS nameVariants,
                pv.sku AS sku,
                pv.regionCode AS regionCode,
                pv.isActive AS isActive,

                p.id AS productId,
                p.name AS productName,

                ii.id AS inventoryId,
                ii.condition AS condition,
                ii.source AS source,
                pv.costPrice AS costPrice,
                pv.salePrice AS salePrice,
                pv.listPrice AS listPrice,
                ii.status AS status,
                ii.stock AS stock,
                ii.warrantyMonths AS warrantyMonths,
                ii.deviceConditionNotes AS deviceConditionNotes

            FROM ProductVariant pv
            JOIN pv.product p
            JOIN pv.inventoryItem ii
            """)
    List<InventoryProjection> getItemInventory();

    @Query("SELECT pv FROM ProductVariant pv WHERE pv.product.id = :id")
    List<ProductVariant> findByProduct(@Param("id") Integer id);
    
}
