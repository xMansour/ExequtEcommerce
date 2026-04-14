package us.exequt.ecommerce.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.exequt.ecommerce.payment.domain.PaymentStatus;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAttemptResultRequest {
    private UUID attemptId;
    private PaymentStatus result;
}
