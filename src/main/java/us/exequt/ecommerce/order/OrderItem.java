package us.exequt.ecommerce.order;

import jakarta.persistence.*;
import lombok.*;
import us.exequt.ecommerce.shared.base.BaseEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    private UUID productId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

}
