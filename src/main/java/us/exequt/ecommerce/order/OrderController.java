package us.exequt.ecommerce.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.exequt.ecommerce.order.dto.OrderResponse;
import us.exequt.ecommerce.shared.RestResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderFacade orderService;

    @GetMapping
    public ResponseEntity<RestResponse<Iterable<OrderResponse>>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAll();
        return ResponseEntity.ok(RestResponse.success("Orders retrieved successfully", orders));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<RestResponse<OrderResponse>> getOrderById(@PathVariable UUID orderId) {
        OrderResponse order = orderService.getById(orderId);
        return ResponseEntity.ok(RestResponse.success("Order with id: " + orderId + " was retrieved successfully", order));
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<RestResponse<OrderResponse>> cancelOrder(@PathVariable UUID orderId) {
        OrderResponse order = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(RestResponse.success("Order with id: " + orderId + " was canceled successfully", order));
    }

    @PostMapping("/{orderId}/payment/start")
    public ResponseEntity<RestResponse<OrderResponse>> payForOrder(@PathVariable UUID orderId) {
        OrderResponse order = orderService.payForOrder(orderId);
        return ResponseEntity.ok(RestResponse.success("Payment for order with id: " + orderId + " was initiated successfully", order));
    }
}
