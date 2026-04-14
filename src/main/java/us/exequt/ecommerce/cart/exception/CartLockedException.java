package us.exequt.ecommerce.cart.exception;

public class CartLockedException extends RuntimeException {
    public CartLockedException(String message) {
        super(message);
    }
}
