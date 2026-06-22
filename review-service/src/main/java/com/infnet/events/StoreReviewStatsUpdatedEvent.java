package com.infnet.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record StoreReviewStatsUpdatedEvent(
        String eventId,
        Long storeId,
        Double averageRating,
        Long totalReviews,
        LocalDateTime dateTime,
        String correlationId
) {
    public static StoreReviewStatsUpdatedEvent reviewStatsUpdated(Long storeId, Double averageRating, Long totalReviews) {
        String correlationId = UUID.randomUUID().toString();
        return new StoreReviewStatsUpdatedEvent(
                UUID.randomUUID().toString(),
                storeId,
                averageRating,
                totalReviews,
                LocalDateTime.now(),
                correlationId
        );
    }
}
