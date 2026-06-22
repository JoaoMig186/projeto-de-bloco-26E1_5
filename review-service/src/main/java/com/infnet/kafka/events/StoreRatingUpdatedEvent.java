package com.infnet.kafka.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record StoreRatingUpdatedEvent(
        String eventId,
        Long storeId,
        Double averageRating,
        Long totalReviews,
        LocalDateTime dateTime,
        String correlationId
) {
    public static StoreRatingUpdatedEvent ratingUpdated(Long storeId, Double averageRating, Long totalReviews) {
        String correlationId = UUID.randomUUID().toString();
        return new StoreRatingUpdatedEvent(
                UUID.randomUUID().toString(),
                storeId,
                averageRating,
                totalReviews,
                LocalDateTime.now(),
                correlationId
        );
    }
}
