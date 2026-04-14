package us.exequt.ecommerce.order;

import org.springframework.data.jpa.repository.JpaRepository;
import us.exequt.ecommerce.order.domain.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
