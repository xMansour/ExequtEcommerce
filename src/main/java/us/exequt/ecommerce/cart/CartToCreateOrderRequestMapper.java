package us.exequt.ecommerce.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import us.exequt.ecommerce.order.dto.CreateOrderRequest;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CartToCreateOrderRequestMapper implements Function<Cart, CreateOrderRequest> {
    private final CartItemToOrderItemRequestMapper cartItemToOrderItemRequestMapper;

    @Override
    public CreateOrderRequest apply(Cart cart) {
        return CreateOrderRequest.builder()
                .cartId(cart.getId())
                .items(cart.getItems().stream()
                        .map(cartItemToOrderItemRequestMapper)
                        .toList())
                .totalPrice(cart.totalPrice())
                .build();
    }
}
