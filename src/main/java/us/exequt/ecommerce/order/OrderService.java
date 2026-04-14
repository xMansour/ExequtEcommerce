package us.exequt.ecommerce.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.exequt.ecommerce.order.dto.CreateOrderRequest;
import us.exequt.ecommerce.order.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderFacade {
    private final OrderRepository orderRepository;
    private final CreateOrderRequestToOrderMapper createOrderRequestToOrderMapper;
    private final OrderToOrderResponseMapper orderToOrderResponseMapper;

    @Override
    public OrderResponse createOrderFromCart(CreateOrderRequest request) {
        return orderToOrderResponseMapper.apply(orderRepository.save(createOrderRequestToOrderMapper.apply(request)));
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
