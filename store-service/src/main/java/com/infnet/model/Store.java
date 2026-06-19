package com.infnet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_stores", schema = "store_service")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String address; // Pode ser evoluído para uma entidade separada no futuro

    private Double latitude;

    private Double longitude;

    private String phone;

    private boolean active = true; // Para inativar uma loja sem deletar os dados

    // Uma loja possui vários produtos.
    // Se a loja for removida, os produtos dela também saem (cascade).
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();
}