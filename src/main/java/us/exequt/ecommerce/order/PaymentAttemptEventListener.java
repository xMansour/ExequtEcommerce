package us.exequt.ecommerce.order;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import us.exequt.ecommerce.payment.PaymentAttemptEvent;

@Component
@RequiredArgsConstructor
public class PaymentAttemptEventListener {
    private final OrderFacade orderFacade;

    @EventListener
    public void onPaymentAttemptResult(PaymentAttemptEvent event) {
        orderFacade.handlePaymentAttemptResult(event.getOrderId(), event.isSuccess());
    }
}
