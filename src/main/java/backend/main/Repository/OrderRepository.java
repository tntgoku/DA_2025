package backend.main.Repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.main.Model.Order.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import backend.main.Model.User.User;

@Repository
public interface OrderRepository extends BaseRepository<Order, Integer> {
    @EntityGraph(attributePaths = { "customer", "orderItems", "orderItems.variant", "orderItems.variant.product" })
    List<Order> findAll();

    List<Order> findByCustomer(User customer);

    @Query("SELECT o FROM Order o WHERE o.customer = :customer")
    List<Order> findListOrdersByIdCustomer(@Param("customer") Integer customer);

    Optional<Order> findByOrderCode(String orderCode);

    @Query("""
                SELECT DISTINCT o FROM Order o
                LEFT JOIN FETCH o.orderItems
                WHERE o.createdAt >= :sevenDaysAgo
                ORDER BY o.createdAt DESC
            """)
    List<Order> findRecentOrders(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);
}
