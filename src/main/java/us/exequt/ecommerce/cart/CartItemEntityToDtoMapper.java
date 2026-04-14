package us.exequt.ecommerce.cart;

import org.springframework.stereotype.Component;
import us.exequt.ecommerce.cart.dto.CartItemResponse;

import java.math.BigDecimal;
import java.util.function.Function;

@Component
public class CartItemEntityToDtoMapper implements Function<CartItem, CartItemResponse> {

    @Override
    public CartItemResponse apply(CartItem cartItem) {
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .totalPrice(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .createdAt(cartItem.getCreatedAt())
                .updatedAt(cartItem.getUpdatedAt())
                .version(cartItem.getVersion())
                .build();
    }
}
