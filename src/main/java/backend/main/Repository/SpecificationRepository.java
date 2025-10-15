package backend.main.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import backend.main.Model.Specification;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpecificationRepository extends BaseRepository<Specification, Integer> {
    List<Specification> findByName(String name);

    // List<Specification> findByNameIn(List<String> names);
}
