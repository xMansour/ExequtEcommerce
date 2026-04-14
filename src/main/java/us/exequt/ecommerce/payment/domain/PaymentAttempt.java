package us.exequt.ecommerce.payment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import us.exequt.ecommerce.shared.base.BaseEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payment_attempts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAttempt extends BaseEntity {
    private UUID orderId;
    private PaymentStatus status;
    private BigDecimal amount;
}
