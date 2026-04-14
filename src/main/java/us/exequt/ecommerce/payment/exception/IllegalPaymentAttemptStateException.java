package us.exequt.ecommerce.payment.exception;

public class IllegalPaymentAttemptStateException extends RuntimeException {
    public IllegalPaymentAttemptStateException(String message) {
        super(message);
    }
}
