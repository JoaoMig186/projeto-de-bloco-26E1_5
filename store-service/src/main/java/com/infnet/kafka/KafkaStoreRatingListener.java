package com.infnet.kafka;

import com.infnet.events.StoreReviewStatsUpdatedEvent;
import com.infnet.model.Store;
import com.infnet.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaStoreRatingListener {
    private final StoreRepository repository;

    @KafkaListener(topics = "icimento.store.review.stats.updated")
    public void receiveStoreReviewStatsUpdated(StoreReviewStatsUpdatedEvent event) {
        Store store = repository.findById(event.storeId())
                .orElseThrow(() -> new RuntimeException("Loja não encontrada: " + event.storeId()));

        store.setAverageRating(event.averageRating());
        store.setTotalReviews(event.totalReviews());
        repository.save(store);
    }
}