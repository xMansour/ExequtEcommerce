package us.exequt.ecommerce.order.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import us.exequt.ecommerce.order.domain.Order;
import us.exequt.ecommerce.order.domain.OrderStatus;
import us.exequt.ecommerce.order.dto.CreateOrderRequest;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CreateOrderRequestToOrderMapper implements Function<CreateOrderRequest, Order> {
    private final OrderItemRequestToOrderItemMapper orderItemRequestToOrderItemMapper;

    @Override
    public Order apply(CreateOrderRequest request) {
        Order order = Order.builder()
                .cartId(request.getCartId())
                .totalPrice(request.getTotalPrice())
                .status(OrderStatus.CREATED)
                .build();

        request.getItems().stream()
                .map(orderItemRequestToOrderItemMapper)
                .forEach(order::addItem);

        return order;
    }
}
