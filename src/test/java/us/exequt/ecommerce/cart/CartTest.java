package us.exequt.ecommerce.cart;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CartTest {
    @Test
    void totalPriceSumsAllItems() {
        Cart cart = new Cart();
        cart.addItem(cartItem(new BigDecimal("10.00"), 2));
        cart.addItem(cartItem(new BigDecimal("5.50"), 3));
        assertThat(cart.totalPrice()).isEqualByComparingTo("36.50");
    }

    @Test
    void totalPriceIsZeroWhenCartIsEmpty() {
        assertThat(new Cart().totalPrice()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    private CartItem cartItem(BigDecimal price, int quantity) {
        return CartItem.builder()
                .price(price)
                .quantity(quantity)
                .build();
    }
}
