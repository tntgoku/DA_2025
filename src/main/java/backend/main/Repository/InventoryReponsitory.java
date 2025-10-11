package backend.main.Repository;

import org.springframework.stereotype.Repository;

import backend.main.Model.InventoryItem;
import java.util.List;

@Repository
public interface InventoryReponsitory extends BaseRepository<InventoryItem, Integer> {
    List<InventoryItem> findByProductVariant(Integer productVariant);
}
