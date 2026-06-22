package com.infnet.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserGeocodeEvent(
        String eventId,
        Long userId,
        Double lat,
        Double lon,
        LocalDateTime dateTime,
        String correlationId
) {
    public static UserGeocodeEvent createEvent(Long userId, Double lat, Double lon){
        String correlationId = UUID.randomUUID().toString();
        return new UserGeocodeEvent(
                UUID.randomUUID().toString(),
                userId,
                lat,
                lon,
                LocalDateTime.now(),
                correlationId
        );
    }
}
