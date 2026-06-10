package com.infnet.events;

import java.time.LocalDateTime;

public record GeocodeEvent(
        String eventId,
        Long userId,
        Double lat,
        Double lon,
        LocalDateTime dateTime,
        String correlationId
){


}