package us.exequt.ecommerce.payment;

public class PaymentAttemptNotFoundException extends RuntimeException {
    public PaymentAttemptNotFoundException(String message) {
        super(message);
    }
}
