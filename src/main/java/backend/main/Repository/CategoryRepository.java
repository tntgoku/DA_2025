package backend.main.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.main.Model.Categories;

@Repository
public interface CategoryRepository extends BaseRepository<Categories, Integer> {
    boolean existsBySlug(String slug);

    @Query("SELECT c FROM Categories c WHERE c.slug = :slug")
    Optional<Categories> findBySlug(@Param("slug") String slug);

    @Query("SELECT c FROM Categories c WHERE c.parent = :parentId ORDER BY c.displayOrder ASC")
    List<Categories> findByParent(@Param("parentId") Integer parentId);

    @Query("SELECT c FROM Categories c WHERE c.slug = :slug AND c.parent = :parentId")
    Optional<Categories> findBySlugAndParent(@Param("slug") String slug, @Param("parentId") Integer parentId);

    @Query("SELECT c FROM Categories c WHERE " +
           "(:keyword IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.slug) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:parentId IS NULL OR c.parent = :parentId) AND " +
           "(:isActive IS NULL OR c.isActive = :isActive) " +
           "ORDER BY c.displayOrder ASC")
    List<Categories> searchCategories(@Param("keyword") String keyword, 
                                     @Param("parentId") Integer parentId, 
                                     @Param("isActive") Boolean isActive, 
                                     Pageable pageable);

    @Query("SELECT c FROM Categories c WHERE c.parent IS NULL ORDER BY c.displayOrder ASC")
    List<Categories> findRootCategories();

    @Query("SELECT c FROM Categories c WHERE c.parent IS NOT NULL ORDER BY c.displayOrder ASC")
    List<Categories> findChildCategories();
}
