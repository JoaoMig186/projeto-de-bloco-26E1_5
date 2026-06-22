package com.infnet.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class StoreMetrics {

    private final Counter storeCreatedCounter;
    private final Counter storeDeactivatedCounter;
    private final Counter productCounter;
    private final MeterRegistry meterRegistry;

    public StoreMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.storeCreatedCounter = Counter.builder("store.events")
                .tag("action", "created")
                .description("Número de lojas registradas")
                .register(meterRegistry);

        this.storeDeactivatedCounter = Counter.builder("store.events")
                .tag("action", "deactivated")
                .description("Número de lojas inativadas")
                .register(meterRegistry);

        this.productCounter = Counter.builder("product.events")
                .tag("action", "created")
                .description("Número total de produtos no catálogo")
                .register(meterRegistry);
    }

    public void incrementStoreCreated() {
        this.storeCreatedCounter.increment();
    }

    public void incrementStoreDeactivated() {
        this.storeDeactivatedCounter.increment();
    }

    public void incrementProductCreation() {
        this.productCounter.increment();
    }

    public void incrementProductCategoryCounter(String categoryName) {
        this.meterRegistry.counter("products.by.category",
                "action", "created",
                "category", categoryName).increment();
    }
}