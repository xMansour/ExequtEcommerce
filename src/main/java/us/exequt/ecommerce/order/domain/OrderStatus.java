package us.exequt.ecommerce.order.domain;

public enum OrderStatus {
    CREATED,
    PENDING_PAYMENT,
    PAYMENT_FAILED,
    PAID,
    CANCELED;

    public boolean canTransitionTo(OrderStatus newStatus) {
        return switch (this) {
            case CREATED -> newStatus == PENDING_PAYMENT || newStatus == CANCELED;
            case PENDING_PAYMENT -> newStatus == PAID || newStatus == PAYMENT_FAILED || newStatus == CANCELED;
            case PAYMENT_FAILED -> newStatus == PENDING_PAYMENT || newStatus == CANCELED;
            case PAID, CANCELED -> false;
        };
    }
}
