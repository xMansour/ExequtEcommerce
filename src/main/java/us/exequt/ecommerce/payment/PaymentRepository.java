package us.exequt.ecommerce.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import us.exequt.ecommerce.payment.domain.PaymentAttempt;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentAttempt, UUID> {
    Optional<PaymentAttempt> findByOrderId(UUID orderId);
}
