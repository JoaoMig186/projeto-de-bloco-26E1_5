package com.infnet.events;

public record StoreCreatedEvent(
        Long storeId,
        String address
) {}