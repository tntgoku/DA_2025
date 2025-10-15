package backend.main.Repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import backend.main.Model.Unit;
import java.util.List;

@Repository
public interface UnitRepository extends BaseRepository<Unit, Integer> {
    Optional<Unit> findByValue(String value);
}
