package com.infnet.kafka;

import com.infnet.kafka.events.StoreRatingUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, StoreRatingUpdatedEvent> kafkaTemplate;

    private void sendEvent(StoreRatingUpdatedEvent event) {
        kafkaTemplate.send("icimento.store.rating.updated",
                String.valueOf(event.storeId()),
                event);
    }

    public void sendStoreRatingUpdatedEvent(Long storeId, Double averageRating, Long totalReviews) {
        StoreRatingUpdatedEvent event = StoreRatingUpdatedEvent.ratingUpdated(storeId, averageRating, totalReviews);
        sendEvent(event);
    }
}
