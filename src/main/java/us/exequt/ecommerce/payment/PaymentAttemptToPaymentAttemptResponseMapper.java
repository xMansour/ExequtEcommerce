package us.exequt.ecommerce.payment;

import org.springframework.stereotype.Component;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResponse;

import java.util.function.Function;

@Component
public class PaymentAttemptToPaymentAttemptResponseMapper implements Function<PaymentAttempt, PaymentAttemptResponse> {
    @Override
    public PaymentAttemptResponse apply(PaymentAttempt paymentAttempt) {
        return PaymentAttemptResponse.builder()
                .id(paymentAttempt.getId())
                .orderId(paymentAttempt.getOrderId())
                .status(paymentAttempt.getStatus())
                .createdAt(paymentAttempt.getCreatedAt())
                .updatedAt(paymentAttempt.getUpdatedAt())
                .version(paymentAttempt.getVersion())
                .build();
    }
}
