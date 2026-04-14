package us.exequt.ecommerce.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import us.exequt.ecommerce.cart.domain.Cart;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
