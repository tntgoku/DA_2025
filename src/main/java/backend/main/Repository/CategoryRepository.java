package backend.main.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.main.Model.Categories;

@Repository
public interface CategoryRepository extends BaseRepository<Categories, Integer> {
    boolean existsBySlug(String slug);

    @Query("SELECT c FROM Categories c WHERE c.slug = :slug")
    Optional<Categories> findBySlug(@Param("slug") String slug);
}
