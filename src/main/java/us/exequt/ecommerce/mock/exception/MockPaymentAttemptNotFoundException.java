package us.exequt.ecommerce.mock.exception;

public class MockPaymentAttemptNotFoundException extends RuntimeException {
    public MockPaymentAttemptNotFoundException(String message) {
        super(message);
    }
}
