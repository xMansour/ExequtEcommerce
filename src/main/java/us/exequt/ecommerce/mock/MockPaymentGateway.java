package us.exequt.ecommerce.mock;

import org.springframework.stereotype.Component;
import us.exequt.ecommerce.payment.PaymentProviderGateway;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MockPaymentGateway implements PaymentProviderGateway {

    final Map<UUID, BigDecimal> pendingAttempts = new ConcurrentHashMap<>();

    @Override
    public void initiatePayment(UUID attemptId, BigDecimal amount) {
        pendingAttempts.put(attemptId, amount);
    }
}
