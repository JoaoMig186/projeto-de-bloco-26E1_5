package car.micro.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrinhos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @OneToMany(
            mappedBy = "carrinho",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemCarrinho> itens = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(updatable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    @PrePersist
    public void prePersist() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        calcularTotal();
    }

    @PreUpdate
    public void preUpdate() {
        dataAtualizacao = LocalDateTime.now();
        calcularTotal();
    }

    public void calcularTotal() {

        total = itens.stream()
                .map(item -> {
                    item.calcularSubtotal();
                    return item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}