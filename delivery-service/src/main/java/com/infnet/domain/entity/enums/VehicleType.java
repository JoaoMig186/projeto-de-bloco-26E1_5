package com.infnet.domain.entity.enums;

import lombok.Getter;

@Getter
public enum VehicleType {

    MOTORCYCLE(20.0),
    PICKUP(100.0),
    TRUCK(Double.MAX_VALUE);

    private final double maxWeight;

    VehicleType(double maxWeight) {
        this.maxWeight = maxWeight;
    }
}