package us.exequt.ecommerce.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import us.exequt.ecommerce.payment.domain.PaymentAttempt;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentAttempt, UUID> {
    List<PaymentAttempt> findByOrderId(UUID orderId);
}
