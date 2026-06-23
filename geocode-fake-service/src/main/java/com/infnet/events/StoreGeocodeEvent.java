package com.infnet.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record StoreGeocodeEvent(
        String eventId,
        Long storeId,
        Double lat,
        Double lon,
        LocalDateTime dateTime,
        String correlationId
) {
    public static StoreGeocodeEvent createEvent(Long storeId, Double lat, Double lon) {
        return new StoreGeocodeEvent(
                UUID.randomUUID().toString(),
                storeId,
                lat,
                lon,
                LocalDateTime.now(),
                UUID.randomUUID().toString()
        );
    }
}