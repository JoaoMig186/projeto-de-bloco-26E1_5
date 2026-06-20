package com.infnet.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMetrics {

    private final Counter deliveriesCreated;
    private final Counter deliveriesFinished;
    private final Counter deliveriesCancelled;

    private final DistributionSummary freightRevenue;

    public DeliveryMetrics(MeterRegistry registry) {

        this.deliveriesCreated =
                Counter.builder("delivery.created.total")
                        .description("Total de entregas criadas")
                        .register(registry);

        this.deliveriesFinished =
                Counter.builder("delivery.finished.total")
                        .description("Total de entregas concluídas")
                        .register(registry);

        this.deliveriesCancelled =
                Counter.builder("delivery.cancelled.total")
                        .description("Total de entregas canceladas")
                        .register(registry);

        this.freightRevenue =
                DistributionSummary.builder("delivery.freight.revenue")
                        .description("Receita total de fretes")
                        .register(registry);
    }

    public void incrementCreated() {
        deliveriesCreated.increment();
    }

    public void incrementFinished() {
        deliveriesFinished.increment();
    }

    public void incrementCancelled() {
        deliveriesCancelled.increment();
    }

    public void recordFreight(Double value) {
        freightRevenue.record(value);
    }
}