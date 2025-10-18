package backend.main.Repository;

import org.springframework.stereotype.Repository;
import java.util.List;

import backend.main.Model.Order.OrderItem;

@Repository
public interface OrderITemRepository extends BaseRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderId(Integer orderId);
}
