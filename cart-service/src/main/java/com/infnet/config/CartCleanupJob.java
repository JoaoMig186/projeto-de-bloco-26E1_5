package com.infnet.config;

import com.infnet.domain.Cart;
import com.infnet.domain.ENUM.CartStatus;
import com.infnet.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CartCleanupJob {

    private final CarRepository repository;

    @Scheduled(fixedRate = 6000000)
    public void cleanExpiredCarts() {

        LocalDateTime cutoffTime =
                LocalDateTime.now().minusMinutes(30);

        List<Cart> carts =
                repository.findByStatusAndUpdatedAtBefore(
                        CartStatus.ENVIADOPARAPAGAMENTO,
                        cutoffTime
                );

        repository.deleteAll(carts);
    }
}