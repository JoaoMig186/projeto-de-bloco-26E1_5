package car.micro.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_carrinho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long produtoId;

    @Column(nullable = false)
    private Long lojaId;

    @Column(nullable = false)
    private String nomeProduto;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer quantidade;

    private Double peso;

    private Boolean fragil = false;

    private String cepLoja;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrinho_id")
    private Carrinho carrinho;

    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (preco != null && quantidade != null) {
            subtotal = preco.multiply(BigDecimal.valueOf(quantidade));
        } else {
            subtotal = BigDecimal.ZERO;
        }
    }
}