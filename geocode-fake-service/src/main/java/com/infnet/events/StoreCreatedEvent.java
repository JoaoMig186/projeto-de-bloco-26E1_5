package com.infnet.events;

import java.time.LocalDateTime;

public record StoreCreatedEvent(
        String eventId,
        Long storeId,
        String address,
        LocalDateTime dateTime,
        String correlationId
) {
}

