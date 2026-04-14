package us.exequt.ecommerce.order.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import us.exequt.ecommerce.order.domain.Order;
import us.exequt.ecommerce.order.dto.OrderResponse;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OrderToOrderResponseMapper implements Function<Order, OrderResponse> {
    private final OrderItemToOrderItemResponseMapper orderItemToOrderItemResponseMapper;

    @Override
    public OrderResponse apply(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .cartId(order.getCartId())
                .status(order.getStatus())
                .items(order.getItems().stream()
                        .map(orderItemToOrderItemResponseMapper)
                        .toList())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .version(order.getVersion())
                .build();
    }
}