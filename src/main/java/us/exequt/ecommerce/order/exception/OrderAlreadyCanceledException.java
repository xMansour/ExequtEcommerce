package us.exequt.ecommerce.order.exception;

public class OrderAlreadyCanceledException extends RuntimeException {
    public OrderAlreadyCanceledException(String message) {
        super(message);
    }
}
