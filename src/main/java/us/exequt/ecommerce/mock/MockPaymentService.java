package us.exequt.ecommerce.mock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.exequt.ecommerce.payment.PaymentFacade;
import us.exequt.ecommerce.payment.PaymentStatus;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResultRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MockPaymentService {
    private final MockPaymentGateway mockPaymentGateway;
    private final PaymentFacade paymentFacade;

    public void confirmPayment(UUID attemptId) {
        if (!mockPaymentGateway.pendingAttempts.containsKey(attemptId))
            throw new MockPaymentAttemptNotFoundException("Payment attempt not found with id: " + attemptId);

        mockPaymentGateway.pendingAttempts.remove(attemptId);
        paymentFacade.processPaymentAttemptResult(
                PaymentAttemptResultRequest.builder()
                        .attemptId(attemptId)
                        .result(PaymentStatus.SUCCESS)
                        .build()
        );
    }

    public void rejectPayment(UUID attemptId) {
        if (!mockPaymentGateway.pendingAttempts.containsKey(attemptId))
            throw new MockPaymentAttemptNotFoundException("Payment attempt not found with id: " + attemptId);

        mockPaymentGateway.pendingAttempts.remove(attemptId);
        paymentFacade.processPaymentAttemptResult(
                PaymentAttemptResultRequest.builder()
                        .attemptId(attemptId)
                        .result(PaymentStatus.FAILED)
                        .build()
        );
    }
}
