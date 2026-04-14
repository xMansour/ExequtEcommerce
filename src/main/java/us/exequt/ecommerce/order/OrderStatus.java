package us.exequt.ecommerce.order;

public enum OrderStatus {
    CREATED,
    PENDING_PAYMENT,
    PAYMENT_FAILED,
    PAID;

    public boolean canTransitionTo(OrderStatus newStatus) {
        return switch (this) {
            case CREATED -> newStatus == PENDING_PAYMENT || newStatus == PAYMENT_FAILED;
            case PENDING_PAYMENT -> newStatus == PAID || newStatus == PAYMENT_FAILED;
            case PAYMENT_FAILED -> newStatus == PENDING_PAYMENT;
            case PAID -> false;
        };
    }
}
