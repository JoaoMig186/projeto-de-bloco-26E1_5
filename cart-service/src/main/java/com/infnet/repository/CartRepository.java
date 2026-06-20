package com.infnet.repository;

import com.infnet.domain.Cart;
import com.infnet.domain.ENUM.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface CarRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByStatusAndDataAtualizacaoBefore(
            CartStatus status,
            LocalDateTime data
    );
    Optional<Cart> findByUsuarioId(Long usuarioId);

    List<Cart> findByStatusAndUpdatedAtBefore(CartStatus cartStatus, LocalDateTime cutoffTime);
}