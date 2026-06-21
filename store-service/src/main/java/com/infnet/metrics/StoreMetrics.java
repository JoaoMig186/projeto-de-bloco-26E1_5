package com.infnet.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class StoreMetrics {

    private final MeterRegistry registry;

    public StoreMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    // Recebe a categoria para criar uma "Tag"
    public void incrementProductCreation(String category) {
        registry.counter("product_creation_total", "category", category).increment();
    }

    public void recordKafkaSyncTime(long milliseconds) {
        registry.timer("kafka_product_sync_time_ms")
                .record(java.time.Duration.ofMillis(milliseconds));
    }
}