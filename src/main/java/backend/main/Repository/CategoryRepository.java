package backend.main.Repository;

import org.springframework.stereotype.Repository;

import backend.main.Model.Categories;

@Repository
public interface CategoryRepository extends BaseRepository<Categories, Integer> {
    boolean existsBySlug(String slug);
}
