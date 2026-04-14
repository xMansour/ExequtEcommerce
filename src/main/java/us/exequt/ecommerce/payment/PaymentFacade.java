package us.exequt.ecommerce.payment;

import us.exequt.ecommerce.payment.dto.PaymentAttemptRequest;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResponse;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResultRequest;
import us.exequt.ecommerce.shared.base.BaseService;

import java.util.UUID;

public interface PaymentFacade extends BaseService<PaymentAttemptResponse, UUID> {
    boolean paymentAttemptAlreadyInProgress(UUID orderId);
    void createPaymentAttempt(PaymentAttemptRequest request);
    PaymentAttemptResponse processPaymentAttemptResult(PaymentAttemptResultRequest request);
}
