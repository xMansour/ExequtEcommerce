package us.exequt.ecommerce.cart.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import us.exequt.ecommerce.cart.CartFacade;
import us.exequt.ecommerce.order.event.OrderCancelledEvent;

@Component
@RequiredArgsConstructor
public class CartOrderEventListener {
    private final CartFacade cartFacade;

    @EventListener
    public void onOrderCancelled(OrderCancelledEvent event) {
        cartFacade.unlockCart(event.getCartId());
    }
}
