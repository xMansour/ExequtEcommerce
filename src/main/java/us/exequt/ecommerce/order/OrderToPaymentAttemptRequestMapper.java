package us.exequt.ecommerce.order;

import org.springframework.stereotype.Component;
import us.exequt.ecommerce.payment.dto.PaymentAttemptRequest;

import java.util.function.Function;

@Component
public class OrderToPaymentAttemptRequestMapper implements Function<Order, PaymentAttemptRequest> {
    @Override
    public PaymentAttemptRequest apply(Order order) {
        return PaymentAttemptRequest.builder()
                .orderId(order.getId())
                .amount(order.getTotalPrice())
                .build();
    }
}
