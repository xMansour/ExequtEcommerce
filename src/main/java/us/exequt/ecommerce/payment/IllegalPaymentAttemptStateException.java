package us.exequt.ecommerce.payment;

public class IllegalPaymentAttemptStateException extends RuntimeException {
    public IllegalPaymentAttemptStateException(String message) {
        super(message);
    }
}
