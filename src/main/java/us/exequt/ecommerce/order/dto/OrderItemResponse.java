package us.exequt.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private UUID id;
    private UUID productId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;
}