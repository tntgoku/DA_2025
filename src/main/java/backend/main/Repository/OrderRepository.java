package backend.main.Repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.main.Model.Order.Order;

import java.util.List;
import backend.main.Model.User.User;

@Repository
public interface OrderRepository extends BaseRepository<Order, Integer> {
    @EntityGraph(attributePaths = { "customer", "orderItems" })
    List<Order> findAll();

    List<Order> findByCustomer(User customer);

    @Query("SELECT o FROM Order o WHERE o.customer = :customer")
    List<Order> findListOrdersByIdCustomer(@Param("customer") Integer customer);

}
