package us.exequt.ecommerce.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResponse;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResultRequest;
import us.exequt.ecommerce.shared.RestResponse;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentFacade paymentService;

    @PostMapping("/webhook")
    public ResponseEntity<RestResponse<PaymentAttemptResponse>> handlePaymentAttemptResult(@RequestBody PaymentAttemptResultRequest request) {
        PaymentAttemptResponse paymentAttempt = paymentService.processPaymentAttemptResult(request);
        return ResponseEntity.ok(RestResponse.success("Payment attempt for order with id: " + paymentAttempt.getOrderId() + " was processed successfully", paymentAttempt));
    }
}
