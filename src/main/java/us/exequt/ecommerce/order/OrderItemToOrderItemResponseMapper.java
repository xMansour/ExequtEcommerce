package us.exequt.ecommerce.order;

import org.springframework.stereotype.Component;
import us.exequt.ecommerce.order.dto.OrderItemResponse;

import java.util.function.Function;

@Component
public class OrderItemToOrderItemResponseMapper implements Function<OrderItem, OrderItemResponse> {
    @Override
    public OrderItemResponse apply(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .totalPrice(orderItem.getTotalPrice())
                .createdAt(orderItem.getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .version(orderItem.getVersion())
                .build();
    }
}
