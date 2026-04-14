package us.exequt.ecommerce.order;

import us.exequt.ecommerce.order.dto.CreateOrderRequest;
import us.exequt.ecommerce.order.dto.OrderResponse;
import us.exequt.ecommerce.shared.base.BaseService;

import java.util.UUID;

public interface OrderFacade extends BaseService<OrderResponse, UUID> {
    OrderResponse createOrderFromCart(CreateOrderRequest request);
}
