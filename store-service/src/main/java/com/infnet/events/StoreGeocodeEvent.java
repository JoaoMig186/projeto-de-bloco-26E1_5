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
}