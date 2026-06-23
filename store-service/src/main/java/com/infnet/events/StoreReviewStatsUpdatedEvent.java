package com.infnet.events;

import java.time.LocalDateTime;

public record StoreReviewStatsUpdatedEvent(
        String eventId,
        Long storeId,
        Double averageRating,
        Long totalReviews,
        LocalDateTime dateTime,
        String correlationId
) {}