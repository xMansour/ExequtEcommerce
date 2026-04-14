package us.exequt.ecommerce.cart;

import org.springframework.stereotype.Component;
import us.exequt.ecommerce.order.dto.OrderItemRequest;

import java.util.function.Function;

@Component
public class CartItemToOrderItemRequestMapper implements Function<CartItem, OrderItemRequest> {
    @Override
    public OrderItemRequest apply(CartItem cartItem) {
        return OrderItemRequest.builder()
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .totalPrice(cartItem.totalPrice())
                .build();
    }
}
