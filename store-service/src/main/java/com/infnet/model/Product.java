package com.infnet.model;

import com.infnet.model.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_products", schema = "store_service")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private BigDecimal price; // BigDecimal é a melhor prática em Java para dados financeiros

    @Column(nullable = false)
    private Integer stockQuantity; // Controla o estoque em tempo real

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category; // Ex: "Pisos", "Cimentos", "Ferragens", conforme o PDF de Objetivo

    // Muitos produtos pertencem a uma única loja
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}