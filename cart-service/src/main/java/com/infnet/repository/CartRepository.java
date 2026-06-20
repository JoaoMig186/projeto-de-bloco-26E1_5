package com.infnet.repository;

import com.infnet.domain.Cart;
import com.infnet.domain.ENUM.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long usuarioId);
    List<Cart> findByStatusAndUpdatedAtBefore(CartStatus status, LocalDateTime cutoffTime);
}