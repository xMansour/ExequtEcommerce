package us.exequt.ecommerce.payment;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentProviderGateway {
    void initiatePayment(UUID attemptId, BigDecimal amount);
}
