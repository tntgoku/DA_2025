package backend.main.Repository;

import org.springframework.stereotype.Repository;

import backend.main.Model.Order.OrderItem;

@Repository
public interface OrderITemRepository extends BaseRepository<OrderItem, Integer> {

}
