package com.infnet.domain;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Long produtoId;
    private Long lojaId;
    private String nomeProduto;
    private BigDecimal preco;
    private Integer quantidade;
    private BigDecimal subtotal;
    private Double peso;
    private Boolean fragil;
    private String nomeLoja;
    private Double latitude;
    private Double longitude;


    public void calcularSubtotal() {
        if (preco == null || quantidade == null) {
            subtotal = BigDecimal.ZERO;
            return;
        }
        subtotal = preco.multiply(BigDecimal.valueOf(quantidade));
    }
}