package com.infnet.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerCreatedEvent(
        String eventId,
        Long userId,
        String address,
        LocalDateTime dateTime,
        String correlationId
) {
    public static CustomerCreatedEvent createEvent(Long userId,String address){
        String correlationId = UUID.randomUUID().toString();
        return new CustomerCreatedEvent(
                UUID.randomUUID().toString(),
                userId,
                address,
                LocalDateTime.now(),
                correlationId
        );
    }
}


