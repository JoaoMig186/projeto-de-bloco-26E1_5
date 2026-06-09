package com.infnet.repository;

import com.infnet.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    // Procura uma loja pelo CNPJ (útil para validação no momento do cadastro)
    Optional<Store> findByCnpj(String cnpj);

    // Retorna apenas as lojas que estão ativas no sistema
    List<Store> findByActiveTrue();
}