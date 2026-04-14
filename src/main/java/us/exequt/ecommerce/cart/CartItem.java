package us.exequt.ecommerce.cart;

import jakarta.persistence.*;
import lombok.*;
import us.exequt.ecommerce.shared.base.BaseEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    private UUID productId;
    private int quantity;
    private BigDecimal price;

    public BigDecimal totalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
