package com.infnet.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record GeocodeEvent(
        String eventId,
        Long userId,
        Double lat,
        Double lon,
        LocalDateTime dateTime,
        String correlationId
) {
    public static GeocodeEvent createEvent(Long userId, Double lat, Double lon){
        String correlationId = UUID.randomUUID().toString();
        return new GeocodeEvent(
                UUID.randomUUID().toString(),
                userId,
                lat,
                lon,
                LocalDateTime.now(),
                correlationId
        );
    }
}
