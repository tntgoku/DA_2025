package backend.main.Repository;

import backend.main.DTO.Product.*;
import backend.main.Model.Product.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {
    @EntityGraph(attributePaths = "variants")
    List<Products> findAll();

    @EntityGraph(attributePaths = { "variants" })
    Optional<Products> findById(Integer id);

    @Query("SELECT pi.id as id, p.name as name, p.productType as productType, pi.isPrimary as isPrimary, pi.productId as productId,pi.variantId as variantId, pi.imageUrl as imageUrl,pi.altImg as altImg, pi.displayOrder as displayOrder  FROM Products p LEFT JOIN  ProductImage pi on p.id = pi.productId  ")
    List<ImageProjection> listNativeImg();

    @Query("""
                SELECT
                    pi.id AS id,
                    p.name AS name,
                    p.productType AS productType,
                    pi.isPrimary AS isPrimary,
                    pi.productId AS productId,
                    pi.variantId AS variantId,
                    pi.imageUrl AS imageUrl,
                    pi.altImg AS altImg,
                    pi.displayOrder AS displayOrder
                FROM Products p
                JOIN ProductImage pi ON p.id = pi.productId
                WHERE pi.productId = :id
            """)
    List<ImageProjection> listNativeImgById(@Param("id") Integer id);

    @EntityGraph(attributePaths = "variants")
    List<Products> findByIsFeaturedTrue();

    Boolean existsBySlug(String slug);

    @Query(value = "EXEC GetProductSpecifications :productId", nativeQuery = true)
    List<Object[]> getProductSpecifications(@Param("productId") int productId);
}