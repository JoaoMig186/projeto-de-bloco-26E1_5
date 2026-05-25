package com.infnet.delivery_sevice.domain.entity.enums;

public enum DeliveryStatus {
    PENDING,
    WAITING_DRIVER,
    DRIVER_ASSIGNED,
    PICKING_UP,
    IN_TRANSIT,
    DELIVERED,
    FAILED,
    CANCELLED
}
