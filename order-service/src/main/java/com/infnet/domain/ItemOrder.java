package com.infnet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(schema = "orders_service", name = "tb_orderitens")
@Entity
@Getter
@Setter
public class ItemOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long itemId;
    Long lojaId;
    String nomeProduto;
    Integer quantidade;
    Double peso;
    Boolean fragil;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public ItemOrder(Long itemId, Long lojaId, String nomeProduto, Boolean fragil, Double peso, Integer quantidade, Order order) {
        this.itemId = itemId;
        this.lojaId = lojaId;
        this.nomeProduto = nomeProduto;
        this.fragil = fragil;
        this.peso = peso;
        this.quantidade = quantidade;
        this.order = order;
    }
    protected ItemOrder() {}
}
