package us.exequt.ecommerce.payment.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.exequt.ecommerce.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAttemptResponse {
    private UUID id;
    private UUID orderId;
    private PaymentStatus status;
    private BigDecimal amount;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;
}
