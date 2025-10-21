package backend.main.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.main.Model.ProductSpecification;

@Repository
public interface ProductSpecificationRepository extends BaseRepository<ProductSpecification, Integer> {

    @Query("SELECT ps FROM ProductSpecification ps WHERE ps.product_id = :productId")
    List<ProductSpecification> findByProductId(@Param("productId") Integer productId);

    @Modifying
    @Query("DELETE FROM ProductSpecification ps WHERE ps.product_id = :productId")
    void deleteByProductId(@Param("productId") Integer productId);

    @Query("SELECT ps FROM ProductSpecification ps WHERE ps.product_id = :productId AND ps.spec_id = :specId")
    Optional<ProductSpecification> findByProductIdAndSpecId(
            @Param("productId") Integer productId,
            @Param("specId") Integer specId);

    @Modifying
    @Query(value = "DELETE FROM ProductSpecification WHERE product_id = :productId", nativeQuery = true)
    void deleteByProductIdNative(@Param("productId") Integer productId);
}