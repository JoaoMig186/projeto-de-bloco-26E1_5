package com.infnet.repository;

import com.infnet.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Lista todos os produtos de uma loja específica (usando o ID da loja)
    List<Product> findByStoreId(Long storeId);

    // Permite a pesquisa de produtos por categoria (ex: "pisos", "cimentos") ignorando maiúsculas/minúsculas
    List<Product> findByCategoryIgnoreCase(String category);

    // Traz os produtos de uma loja específica, filtrando também por categoria
    List<Product> findByStoreIdAndCategoryIgnoreCase(Long storeId, String category);
}