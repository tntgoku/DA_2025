package backend.main.Repository;

import backend.main.Model.Product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    
    List<ProductImage> findByProductId(Integer productId);
    
    List<ProductImage> findByVariantId(Integer variantId);
    
    List<ProductImage> findByProductIdAndVariantId(Integer productId, Integer variantId);
    
    List<ProductImage> findByProductIdOrderByDisplayOrder(Integer productId);
    
    List<ProductImage> findByVariantIdOrderByDisplayOrder(Integer variantId);
    
    List<ProductImage> findByIsPrimaryTrue();
    
    List<ProductImage> findByProductIdAndIsPrimaryTrue(Integer productId);
    
    List<ProductImage> findByVariantIdAndIsPrimaryTrue(Integer variantId);
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.productId = :productId AND pi.variantId = 0")
    List<ProductImage> findProductMainImages(@Param("productId") Integer productId);
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.productId = :productId")
    List<ProductImage> findProductVariantImages(@Param("productId") Integer productId);
    @Modifying
    @Query("DELETE FROM ProductImage pi WHERE pi.productId = :productId")
    void deleteByProductId(@Param("productId") Integer productId);
    
    @Modifying
    @Query("DELETE FROM ProductImage pi WHERE pi.variantId = :variantId")
    void deleteByVariantId(@Param("variantId") Integer variantId);
    
    @Modifying
    @Query("DELETE FROM ProductImage pi WHERE pi.productId = :productId AND pi.variantId = :variantId")
    void deleteByProductIdAndVariantId(@Param("productId") Integer productId, @Param("variantId") Integer variantId);
    
}
