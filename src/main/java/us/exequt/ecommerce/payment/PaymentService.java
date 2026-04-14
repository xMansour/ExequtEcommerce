package us.exequt.ecommerce.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import us.exequt.ecommerce.order.OrderCancelledEvent;
import us.exequt.ecommerce.payment.dto.PaymentAttemptRequest;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResponse;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResultRequest;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentFacade {
    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PaymentAttemptToPaymentAttemptResponseMapper paymentAttemptToPaymentAttemptResponseMapper;
    private final PaymentAttemptRequestToPaymentAttemptMapper paymentAttemptRequestToPaymentAttemptMapper;

    @Override
    public boolean paymentAttemptAlreadyInProgress(UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .anyMatch(paymentAttempt -> paymentAttempt.getStatus() == PaymentStatus.PENDING);
    }

    @Override
    public void createPaymentAttempt(PaymentAttemptRequest request) {
        paymentAttemptToPaymentAttemptResponseMapper.apply(paymentRepository.save(paymentAttemptRequestToPaymentAttemptMapper.apply(request)));
    }

    @Override
    public PaymentAttemptResponse processPaymentAttemptResult(PaymentAttemptResultRequest request) {
        PaymentAttempt paymentAttempt = paymentRepository.findById(request.getAttemptId())
                .orElseThrow(() -> new PaymentAttemptNotFoundException("Payment attempt not found with id: " + request.getAttemptId()));

        if (paymentAttempt.getStatus() != PaymentStatus.PENDING)
            throw new IllegalPaymentAttemptStateException("Payment attempt with id: " + request.getAttemptId() + " is already processed with status: " + paymentAttempt.getStatus());

        paymentAttempt.setStatus(request.getResult());
        PaymentAttempt savedAttempt = paymentRepository.save(paymentAttempt);
        eventPublisher.publishEvent(PaymentAttemptEvent.builder()
                .orderId(savedAttempt.getOrderId())
                .success(savedAttempt.getStatus() == PaymentStatus.SUCCESS)
                .build());
        return paymentAttemptToPaymentAttemptResponseMapper.apply(savedAttempt);
    }

    @Override
    public PaymentAttemptResponse getById(UUID id) {
        return paymentRepository.findById(id)
                .map(paymentAttemptToPaymentAttemptResponseMapper)
                .orElseThrow(() -> new PaymentAttemptNotFoundException("Payment attempt not found with id: " + id));
    }

    @Override
    public List<PaymentAttemptResponse> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(paymentAttemptToPaymentAttemptResponseMapper)
                .toList();
    }
}
