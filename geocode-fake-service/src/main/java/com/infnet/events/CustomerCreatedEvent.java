package com.infnet.events;

import java.time.LocalDateTime;

public record CustomerCreatedEvent(
        String eventId,
        Long userId,
        String address,
        LocalDateTime dateTime,
        String correlationId
) {
}
