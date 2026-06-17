package car.micro.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrinho_id")
    @JsonIgnore
    private Carrinho carrinho;

    public void calcularSubtotal() {
        if (preco == null || quantidade == null) {
            subtotal = BigDecimal.ZERO;
            return;
        }

        subtotal = preco.multiply(BigDecimal.valueOf(quantidade));
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        calcularSubtotal();
    }
}