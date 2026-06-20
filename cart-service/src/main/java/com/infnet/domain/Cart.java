package com.infnet.domain;

import com.infnet.domain.ENUM.CartStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    private List<CartItem> items = new ArrayList<>();

    private BigDecimal total = BigDecimal.ZERO;

    private BigDecimal totalWeightKg = BigDecimal.ZERO;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = CartStatus.OPEN;
        }

        calculateTotal();
        calculateTotalWeight();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        calculateTotal();
        calculateTotalWeight();
    }

    public void calculateTotal() {
        total = items.stream()
                .map(item -> {
                    item.calculateSubtotal();
                    return item.getSubtotal();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void calculateTotalWeight() {
        totalWeightKg = items.stream()
                .map(item -> {
                    if (item.getWeight() == null || item.getQuantity() == null) {
                        return BigDecimal.ZERO;
                    }

                    return BigDecimal.valueOf(item.getWeight())
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}