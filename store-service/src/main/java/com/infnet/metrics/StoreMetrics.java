package com.infnet.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class StoreMetrics {

    private final Counter productCreationCounter;

    public StoreMetrics(MeterRegistry registry) {
        this.productCreationCounter = Counter.builder("product_creation_total")
                .description("Total de produtos criados e sincronizados na loja")
                .register(registry);
    }

    public void incrementProductCreation() {
        productCreationCounter.increment();
    }
}