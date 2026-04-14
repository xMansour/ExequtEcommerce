package us.exequt.ecommerce.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.exequt.ecommerce.cart.dto.AddCartItemRequest;
import us.exequt.ecommerce.cart.dto.CartResponse;
import us.exequt.ecommerce.shared.RestResponse;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartFacade cartService;

    @PostMapping
    public ResponseEntity<RestResponse<CartResponse>> createCart() {
        CartResponse cart = cartService.createCart();
        return ResponseEntity
                .created(URI.create("/carts/" + cart.getId()))
                .body(RestResponse.success("Cart created successfully", cart));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<RestResponse<CartResponse>> addItemToCart(@PathVariable UUID cartId, @RequestBody AddCartItemRequest request) {
        CartResponse updatedCart = cartService.addItemToCart(cartId, request);
        return ResponseEntity.ok(RestResponse.success("Item added to cart successfully", updatedCart));
    }

    @PatchMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<RestResponse<CartResponse>> updateItemInCart(@PathVariable UUID cartId, @PathVariable UUID itemId, @RequestParam int quantity) {
        CartResponse updatedCart = cartService.updateItemInCart(cartId, itemId, quantity);
        return ResponseEntity.ok(RestResponse.success("Item with id: " + itemId + " was updated successfully", updatedCart));
    }

    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<RestResponse<CartResponse>> checkout(@PathVariable UUID cartId) {
        CartResponse lockedCart = cartService.checkout(cartId);
        return ResponseEntity.ok(RestResponse.success("Cart with id: " + cartId + " was checked out successfully", lockedCart));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<RestResponse<CartResponse>> getCartById(@PathVariable UUID cartId) {
        CartResponse cart = cartService.getById(cartId);
        return ResponseEntity.ok(RestResponse.success("Cart with id: " + cartId + " was retrieved successfully", cart));
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<CartResponse>>> getAllCarts() {
        List<CartResponse> carts = cartService.getAll();
        return ResponseEntity.ok(RestResponse.success("Carts retrieved successfully", carts));
    }
}
