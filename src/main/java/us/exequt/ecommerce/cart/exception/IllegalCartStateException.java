package us.exequt.ecommerce.cart.exception;

public class IllegalCartStateException extends RuntimeException {
    public IllegalCartStateException(String message) {
        super(message);
    }
}
