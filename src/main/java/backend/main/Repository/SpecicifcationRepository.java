package backend.main.Repository;

import org.springframework.stereotype.Repository;

import backend.main.Model.Specification;
import java.util.List;

@Repository
public interface SpecicifcationRepository extends BaseRepository<Specification, Integer> {

    List<Specification> findByCategoryId(Integer categoryId);
}
