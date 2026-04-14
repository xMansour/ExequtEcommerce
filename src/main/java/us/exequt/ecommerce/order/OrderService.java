package us.exequt.ecommerce.order;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import us.exequt.ecommerce.order.dto.CreateOrderRequest;
import us.exequt.ecommerce.order.dto.OrderResponse;
import us.exequt.ecommerce.payment.PaymentFacade;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderFacade {
    private final PaymentFacade paymentService;
    private final ApplicationEventPublisher eventPublisher;
    private final OrderRepository orderRepository;
    private final CreateOrderRequestToOrderMapper createOrderRequestToOrderMapper;
    private final OrderToOrderResponseMapper orderToOrderResponseMapper;
    private final OrderToPaymentAttemptRequestMapper orderToPaymentAttemptRequestMapper;

    @Override
    public void createOrderFromCart(CreateOrderRequest request) {
        orderToOrderResponseMapper.apply(orderRepository.save(createOrderRequestToOrderMapper.apply(request)));
    }

    @Override
    public OrderResponse cancelOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        if (order.getStatus() == OrderStatus.CANCELED)
            throw new OrderAlreadyCanceledException("Order with id: " + id + " is already canceled");

        if (!order.getStatus().canTransitionTo(OrderStatus.CANCELED))
            throw new IllegalOrderStateException("Order with id: " + id + " cannot be canceled from status: " + order.getStatus());

        order.setStatus(OrderStatus.CANCELED);
        Order savedOrder = orderRepository.save(order);
        eventPublisher.publishEvent(OrderCancelledEvent.builder()
                .orderId(savedOrder.getId())
                .cartId(savedOrder.getCartId())
                .build());
        return orderToOrderResponseMapper.apply(savedOrder);
    }

    @Override
    public OrderResponse payForOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        if (!order.getStatus().canTransitionTo(OrderStatus.PENDING_PAYMENT))
            throw new IllegalOrderStateException("Payment for order with id: " + id + " cannot be started from status: " + order.getStatus());

        // check if no payment attempt is already in progress (idempotency check)
        if (paymentService.paymentAttemptAlreadyInProgress(id))
            throw new IllegalOrderStateException("Payment for order with id: " + id + " is already in progress");

        order.setStatus(OrderStatus.PENDING_PAYMENT);
        Order savedOrder = orderRepository.save(order);
        paymentService.createPaymentAttempt(orderToPaymentAttemptRequestMapper.apply(savedOrder));
        return orderToOrderResponseMapper.apply(savedOrder);
    }

    @Override
    public void handlePaymentAttemptResult(UUID orderId, boolean success) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT)
            throw new IllegalOrderStateException("Payment attempt result for order with id: " + orderId + " cannot be processed from status: " + order.getStatus());

        order.setStatus(success ? OrderStatus.PAID : OrderStatus.PAYMENT_FAILED);
        orderRepository.save(order);
    }

    @Override
    public OrderResponse getById(UUID id) {
        return orderToOrderResponseMapper.apply(orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id)));
    }

    @Override
    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream()
                .map(orderToOrderResponseMapper)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        if (!orderRepository.existsById(id))
            throw new OrderNotFoundException("Order not found with id: " + id);
        orderRepository.deleteById(id);
    }
}
