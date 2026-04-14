package us.exequt.ecommerce.order.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import us.exequt.ecommerce.order.OrderFacade;
import us.exequt.ecommerce.payment.event.PaymentAttemptEvent;

@Component
@RequiredArgsConstructor
public class PaymentAttemptEventListener {
    private final OrderFacade orderFacade;

    @EventListener
    public void onPaymentAttemptResult(PaymentAttemptEvent event) {
        orderFacade.handlePaymentAttemptResult(event.getOrderId(), event.isSuccess());
    }
}
