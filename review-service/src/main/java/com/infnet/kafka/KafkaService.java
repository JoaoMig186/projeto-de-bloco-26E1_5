package com.infnet.kafka;

import com.infnet.events.StoreReviewStatsUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, StoreReviewStatsUpdatedEvent> kafkaTemplate;

    private void sendEvent(StoreReviewStatsUpdatedEvent event) {
        kafkaTemplate.send("icimento.store.review.stats.updated",
                String.valueOf(event.storeId()),
                event);
    }

    public void sendStoreRatingUpdatedEvent(Long storeId, Double averageRating, Long totalReviews) {
        StoreReviewStatsUpdatedEvent event = StoreReviewStatsUpdatedEvent.reviewStatsUpdated(storeId, averageRating, totalReviews);
        sendEvent(event);
    }
}
