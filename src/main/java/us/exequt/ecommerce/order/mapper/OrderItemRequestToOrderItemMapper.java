package us.exequt.ecommerce.order.mapper;

import org.springframework.stereotype.Component;
import us.exequt.ecommerce.order.domain.OrderItem;
import us.exequt.ecommerce.order.dto.OrderItemRequest;

import java.util.function.Function;

@Component
public class OrderItemRequestToOrderItemMapper implements Function<OrderItemRequest, OrderItem> {
    @Override
    public OrderItem apply(OrderItemRequest orderItemRequest) {
        return OrderItem.builder()
                .productId(orderItemRequest.getProductId())
                .quantity(orderItemRequest.getQuantity())
                .price(orderItemRequest.getPrice())
                .totalPrice(orderItemRequest.getTotalPrice())
                .build();
    }
}
