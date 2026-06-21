package com.infnet.config;

import com.infnet.domain.Cart;
import com.infnet.domain.ENUM.CartStatus;
import com.infnet.metrics.CartMetrics;
import com.infnet.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CartCleanupJob {

    private final CartRepository repository;
    private final CartMetrics metrics;

    @Scheduled(fixedRate = 60000)
    public void manageCartLifecycle() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime cutoffForClose = now.minusMinutes(15);
        List<Cart> cartsToClose = repository.findByStatusAndUpdatedAtBefore(
                CartStatus.ENVIADOPARAPAGAMENTO,
                cutoffForClose
        );

        if (!cartsToClose.isEmpty()) {
            cartsToClose.forEach(cart -> {
                cart.setStatus(CartStatus.CLOSE);
                metrics.incrementarCarrinhosFinalizados();
            });
            repository.saveAll(cartsToClose);
        }

        LocalDateTime cutoffForDelete = now.minusMinutes(30);
        List<Cart> cartsToDelete = repository.findByStatusAndUpdatedAtBefore(
                CartStatus.CLOSE,
                cutoffForDelete
        );

        if (!cartsToDelete.isEmpty()) {
            repository.deleteAll(cartsToDelete);
        }
    }
}