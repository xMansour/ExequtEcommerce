package us.exequt.ecommerce.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResponse;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResultRequest;
import us.exequt.ecommerce.shared.RestResponse;

import java.util.List;
import java.util.UUID;

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

    @GetMapping
    public ResponseEntity<RestResponse<List<PaymentAttemptResponse>>> getAllPaymentAttempts() {
        List<PaymentAttemptResponse> payments = paymentService.getAll();
        return ResponseEntity.ok(RestResponse.success("Payments retrieved successfully", payments));
    }

    @GetMapping("/{paymentAttemptId}")
    public ResponseEntity<RestResponse<PaymentAttemptResponse>> getOrderById(@PathVariable UUID paymentAttemptId) {
        PaymentAttemptResponse payment = paymentService.getById(paymentAttemptId);
        return ResponseEntity.ok(RestResponse.success("Payment attempt with id: " + paymentAttemptId + " retrieved successfully", payment));
    }
}
