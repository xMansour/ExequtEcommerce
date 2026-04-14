package us.exequt.ecommerce.payment;

import org.springframework.stereotype.Component;
import us.exequt.ecommerce.payment.dto.PaymentAttemptRequest;

import java.util.function.Function;

@Component
public class PaymentAttemptRequestToPaymentAttemptMapper implements Function<PaymentAttemptRequest, PaymentAttempt> {
    @Override
    public PaymentAttempt apply(PaymentAttemptRequest request) {
        return PaymentAttempt.builder()
                .orderId(request.getOrderId())
                .status(PaymentStatus.PENDING)
                .amount(request.getAmount())
                .build();
    }
}
