package us.exequt.ecommerce.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import us.exequt.ecommerce.cart.dto.CartResponse;

import java.math.BigDecimal;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CartEntityToDtoMapper implements Function<Cart, CartResponse> {
    private final CartItemEntityToDtoMapper cartItemEntityToDtoMapper;

    @Override
    public CartResponse apply(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .items(cart.getItems()
                        .stream()
                        .map(cartItemEntityToDtoMapper)
                        .toList())
                .status(cart.getStatus())
                .totalPrice(cart.getItems().stream()
                        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO))
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .version(cart.getVersion())
                .build();
    }
}
