package us.exequt.ecommerce.cart.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.exequt.ecommerce.cart.CartStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartResponse {
    private UUID id;
    private List<CartItemResponse> items;
    private CartStatus status;
    private BigDecimal totalPrice;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;
}
