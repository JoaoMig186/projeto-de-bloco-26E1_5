package com.infnet.domain;

import com.infnet.DTO.StoreDTO;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;
import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Long productId;
    private Long storeId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
    private Double weight;
    private Boolean fragile;
    @Embedded
    private StoreDTO store;

    public void calculateSubtotal() {
        if (price == null || quantity == null) {
            subtotal = BigDecimal.ZERO;
            return;
        }
        subtotal = price.multiply(BigDecimal.valueOf(quantity));
    }
}