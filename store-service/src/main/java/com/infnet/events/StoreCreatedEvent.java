package com.infnet.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record StoreCreatedEvent(
        String eventId,
        Long storeId,
        String address,
        LocalDateTime dateTime,
        String correlationId
) {
    public static StoreCreatedEvent createEvent(Long storeId, String address) {
        return new StoreCreatedEvent(
                UUID.randomUUID().toString(),
                storeId,
                address,
                LocalDateTime.now(),
                UUID.randomUUID().toString()
        );
    }
}