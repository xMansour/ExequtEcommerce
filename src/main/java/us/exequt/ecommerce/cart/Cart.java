package us.exequt.ecommerce.cart;

import jakarta.persistence.*;
import lombok.*;
import us.exequt.ecommerce.shared.base.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private CartStatus status = CartStatus.ACTIVE;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        item.setCart(this);
        this.items.add(item);
    }

    public BigDecimal totalPrice() {
        return items.stream()
                .map(CartItem::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
