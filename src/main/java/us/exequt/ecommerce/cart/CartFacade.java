package us.exequt.ecommerce.cart;

import us.exequt.ecommerce.cart.dto.AddCartItemRequest;
import us.exequt.ecommerce.cart.dto.CartResponse;
import us.exequt.ecommerce.shared.base.BaseService;

import java.util.UUID;

public interface CartFacade extends BaseService<CartResponse, UUID> {
    CartResponse addItemToCart(UUID cartId, AddCartItemRequest request);
    CartResponse updateItemInCart(UUID cartId, UUID itemId, int quantity);
    CartResponse checkout(UUID cartId);
}
