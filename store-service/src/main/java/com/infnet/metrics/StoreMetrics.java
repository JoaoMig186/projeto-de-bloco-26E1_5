package com.infnet.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class StoreMetrics {

    private final MeterRegistry registry;

    public StoreMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void incrementProductCreation(String category) {
        registry.counter("product_creation_total", "category", category).increment();
    }

}